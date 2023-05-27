import argparse

from utils.constant import *
from utils.logger import *
from utils.mailer import NotifyToUsers
from utils.utils import Utils


class Runner:

    def __init__(self):
        parse = argparse.ArgumentParser()
        parse.add_argument("--tags", required=False, type=str, help="Provide the tag(s) name.")
        parse.add_argument("--module", required=False, type=str, help="Provide the tag(s) name.")
        parse.add_argument("--features", required=False, type=str, help="Provide the feature name.")
        args = parse.parse_args()

        self.tags = args.tags
        self.module = args.module
        self.features = args.features
        self.job_id = FRAMWORK_NAME + "_" + datetime.now().strftime(TIMEFORMAT)

        logger(f" Version:{FW_VERSION} ".center(80, "*"), LOGINFO, self.job_id)
        logger(f"Execution Job Id:{self.job_id}", LOGINFO, self.job_id)
        logger(f"Tag:{self.tags} || Module:{self.module} || Feature:{self.features}", LOGINFO, self.job_id)

    def trigger_job(self):
        # Allure
        self.root_dir = os.getcwd()
        # self.reportpath = self.root_dir + os.sep + "reports" + os.sep + self.job_id + os.sep + "results"
        self.reportpath = self.root_dir + os.sep + "allure-results"
        if not os.path.isdir(self.reportpath):
            os.makedirs(self.reportpath)
        self.allurereport_param = f"-f allure_behave.formatter:AllureFormatter -o {self.reportpath}"

        # Final Behave command
        self.utils = Utils(self.job_id)
        if self.features != None:
            self.feature_exec_param = self.utils.execute_by_feature_file_param(self.module, self.features)
            logger(f"Feature Files for execution:{self.feature_exec_param}", LOGINFO, self.job_id)
            final_command = f"behave {self.feature_exec_param} {self.allurereport_param} -D job_id={self.job_id} -D module={self.module}"
        elif self.tags == None:
            final_command = f"behave .//modules//{self.module}  {self.allurereport_param} -D job_id={self.job_id} -D module={self.module}"
        else:
            final_command = f"behave .//modules//{self.module} --tags {self.tags} {self.allurereport_param} -D job_id={self.job_id} -D module={self.module}"
        logger(f"Final command:{final_command}", LOGINFO, self.job_id)

        # Execute Behave command
        os.system(final_command)

        logger(f"Sending Mail...", LOGINFO, self.job_id)
        if not NotifyToUsers(self.reportpath):
            logger(f"Failed to send mail!", LOGINFO, self.job_id)
        else:
            logger(f"Mail Sent Successfully", LOGINFO, self.job_id)
        logger(f"Execution Job_ID:{self.job_id}", LOGINFO, self.job_id)


if __name__ == "__main__":
    mainobj = Runner()
    mainobj.trigger_job()
