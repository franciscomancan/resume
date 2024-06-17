import cx_Oracle
from sqlalchemy import create_engine
import pandas as pd

table = 'EMPLOYEE'

# for oracle, need to distinguish connection string by SID or SERVICENAME
# sid => "oracle+cx_oracle://user:pass@localhost:1521/xe"
# service name => "oracle+cx_oracle://SOMEONE:SOMETHING@localhost:1521/?service_name=xepdb"
connection_string = f"oracle+cx_oracle://SOMEONE:SOMETHING@localhost:1521/?service_name=xepdb1"

sql_query = f"SELECT * FROM {table}"
try:

  engine = create_engine(connection_string)
  df = pd.read_sql(sql_query, engine)

except Exception as error:
  print(f"Error connecting to Oracle database: {error}")

else:
  print(df.head())  # Print the first few rows
  df.to_csv(f"{table}-extract.csv", index=False, header=True)

print("Finished processing data.")
