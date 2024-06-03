from airflow import DAG
from airflow.operators.python import PythonOperator
from airflow.utils.dates import days_ago
from airflow.utils.session import provide_session
from airflow.models import DagRun
from airflow.utils.state import DagRunState
from datetime import timedelta


"""
Gather average successful runtime for dag collection to monitor long-term growth.
"""

dag = DAG(
    dag_id='calc_cer_runtime_stats',
    default_args={
        'owner': 'airflow_entity_resolution',
        'depends_on_past': False,
        'start_date': days_ago(1),
        'retries': 0,
    }
)


@provide_session
def get_runtimes(dag_id, session=None):
    dag_runs = session.query(DagRun).filter(
        DagRun.dag_id == dag_id,
        DagRun.state == DagRunState.SUCCESS,
    ).order_by(DagRun.execution_date.desc())

    runtimes = [run.end_date - run.start_date for run in dag_runs]
    return runtimes


cer_dags = ['sftp_to_s3_transfer','daily_delta_etl','preprocessing_pipelines', 'cer_pipelines','s3_to_sftp_transfer','profiler_pipeline']

def sum_timedeltas(timedeltas):
  total_delta = timedelta(seconds=0)
  for delta in timedeltas:
    total_delta += delta
  return total_delta


def calculate_average_runtime():
    print(f"dags => {str(cer_dags)}")
    for dag_id in cer_dags:
        runtimes = get_runtimes(dag_id)
        if runtimes:
            total_delta = sum_timedeltas(runtimes)
            total_runs = len(runtimes)
            average_runtime_secs = total_delta.total_seconds() / total_runs
            average_runtime_mins = average_runtime_secs / 60
            print(f"Average `Successful` Runtime ({dag_id}): {average_runtime_mins:.2f} minutes,  total runs: {total_runs}")
        else:
            print(f"No successful DAG runs found for calculation ({dag_id}).")


calculate_average_runtime_task = PythonOperator(
    task_id='calculate_average_runtime',
    provide_context=True,
    python_callable=calculate_average_runtime,
    dag=dag
)