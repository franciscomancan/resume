import csv
from simple_salesforce import Salesforce, SalesforceResourceNotFound
from resources import source_config


def extract():
    sources = source_config.get_salesforce_sources()
    base_fields = ["Id", "Name"]
    for source in sources:
        conn = source_config.get_salesforce_connection(source)
        objs = source_config.get_salesforce_objects(source)
        print(conn)
        print(objs)
        for obj in objs:
            salesforce_object = obj.get('name')
            sf = Salesforce(username=conn.get('username'), password=conn.get('password'),
                            security_token=conn.get('security_token'), instance_url=conn.get('instance_url'))

            try:
                if salesforce_object == 'Contact':
                    fields = get_contact_fields(sf)
                elif salesforce_object == 'Product':
                    fields = get_product_fields(sf)
                else:
                    fields = base_fields

                # SOQL = salesforce object query language
                soql_query = f"SELECT {', '.join(fields)} FROM {salesforce_object}"

                data = sf.query(soql_query)["records"]
                if not data:
                    print(f"No records found for object: {salesforce_object}")
                    exit(1)

                with open(f"{salesforce_object}_data.csv", "w", newline="") as csvfile:
                    writer = csv.writer(csvfile)
                    writer.writerow(fields)
                    for record in data:
                        row = [record[field] for field in fields]
                        writer.writerow(row)

                print(f"Successfully extracted data from {salesforce_object} and saved to {salesforce_object}_data.csv")

            except SalesforceResourceNotFound as e:
                print(f"Resource not found: {str(e)}")




"""
Top 10 objects:Account, Contact, Opportunity, Lead, Product, Campaign, Case, User, Contract, Report
"""
def get_contact_fields(salesforce_object):
    desc = salesforce_object.Contact.describe()
    #field_labels = [field['label'] for field in desc['fields']]
    return [field['name'] for field in desc['fields']]
    #field_types = [field['type'] for field in desc['fields']]

def get_product_fields(salesforce_object):
    desc = salesforce_object.Product.describe()
    return [field['name'] for field in desc['fields']]

extract()