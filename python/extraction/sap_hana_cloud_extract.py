import csv
import sqlalchemy
from sqlalchemy import create_engine, Table, select, text

# Replace placeholders with your actual details
HOST = "........hanacloud.ondemand.com"  # Replace with your SAP HANA Cloud hostname
PORT = 443  # Standard port for secure connections
USER = "BOGUS"  # Replace with your HANA Cloud username
PASSWORD = "123456789"  # Replace with your HANA Cloud password
DATABASE = "H00"  # Replace with your HANA Cloud database name

# Construct the connection string with HANA ML dialect
connection_string = f"hana://{USER}:{PASSWORD}@{HOST}:{PORT}/{DATABASE}"

try:
  engine = create_engine(connection_string)
  metadata = sqlalchemy.MetaData()
  metadata.reflect(engine)
  table_names = [table.name for table in metadata.tables.values()]

#--------------------------------------------------------------------------------

  print(f"List of tables (and potentially other schema objects):")
  for table_name, table in metadata.tables.items():
    query = text(f"select top 100 * from {table_name}")
    file_out = f"./output/{table_name}.csv"
    with open(file_out, 'w', newline='', encoding="utf-8") as csvfile:
      csv_writer = csv.writer(csvfile)
      with engine.connect() as conn:
        result = conn.execute(query)
        all_records = result.fetchall()

        csv_writer.writerow([column.name for column in table.columns])   # write header
        for row in all_records:
          csv_writer.writerow(row)

        print(f"Number of records to {file_out}: {len(all_records)}")



  # problem here - sap does not honor/populate the schema attribute, need to find another way at the column names
  # sap also does not populate the cursor.description attribute, so not an optin either
  #my_table = Table(table_name, engine, metadata=metadata)

  #query = select([my_table.columns])      # retrieve all columns


finally:
  engine.dispose()  # Close the connection pool
  print("Connection closed.")
