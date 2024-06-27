def get_subdomains_list_standalone():
    try:
        subdomains_list = []
        test_file = '../resources/domains_subdomains.csv'       # TODO: move this out

        df = pd.read_csv(test_file, header=0)
        for index, row in df.iterrows():
            sd = {
                "domain": row['domain'],
                "sub_domain": row['subdomain'],
                "subdomain_description": row['subdomain_description'],
                "dataproduct": row['data_product'],
                "dataproduct_description": row['data_product_description']
            }
            subdomains_list.append(sd)

        return subdomains_list

    except Exception as e:
        print(str(e))
