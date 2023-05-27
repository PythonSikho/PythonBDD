import email, smtplib, ssl
import json
import os
import subprocess
import time
from email import encoders
from email.mime.base import MIMEBase
import smtplib, ssl
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

from utils.cred import *
from utils.logger import *
import shutil


def deployAllureReportToNetlify(resultPath):
    print("Please wait. Generating Report...")

    # Step-1
    print("Generating allure report...")
    reportPath = resultPath + os.sep + "allure-report"
    os.system(f"C:\\allure-2.22.0\\allure-2.22.0\\bin\\allure generate {resultPath} -o {reportPath} --clean")

    # Step-2
    print("Zipping the allure-report for Netify")
    shutil.make_archive(reportPath, 'zip', reportPath)

    # Step-3  Upload/Deploy allure.zip to netlify
    allurezip = resultPath + os.sep + "allure-report.zip"
    curl_cmd = f'curl -H "Content-Type: application/zip" -H "Authorization: Bearer {NetlifyToken}" --data-binary "@{allurezip}" https://api.netlify.com/api/v1/sites'
    print("Uploading the allure to Netify...")

    os.system(curl_cmd)
    time.sleep(15)

    print("Deployed to netlify.")


# step-4 {Link geneartion}
def get_netlify_report_link():
    print("Fetching Netlify link")
    global netlifyReportLink
    try:
        command = f'curl -H "User-Agent: MyApp ({NetlifyUsername})" -H "Authorization: Bearer {NetlifyToken}" https://api.netlify.com/api/v1/sites'

        proc = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
        data, err = proc.communicate()
        data = data.decode("UTF-8")
        err = err.decode("UTF-8")
        response = json.loads(data)[0]
        netlifyReportLink = response["published_deploy"]["links"]["alias"]
        print(f"Netlify Report Link:{netlifyReportLink}")
    except Exception as err:
        netlifyReportLink = "https://api.netlify.com/api/v1/sites"
        print(err)
    return netlifyReportLink


def send_automation_report(netlifyReportLink=NetlifySiteOverView, attachHtmlReport=None):
    print("Initiating Sending Mail...")
    message = MIMEMultipart()
    message["From"] = sender_email
    message["To"] = receiver_email
    message["Subject"] = "BDD Automation Report"
    message["Bcc"] = receiver_email

    body = f"Allure Report Link:{netlifyReportLink}\n"
    html = """\
    <html>
      <body>
       <h1>BDD Automation Report</h1> 
       <h3>Execution Completed. Below is the allure report link</h3>    
      </body>
    </html>
    """

    part1 = MIMEText(body, "plain")
    part2 = MIMEText(html, "html")

    if attachHtmlReport != None:
        print("Attaching the html report .")
        try:
            with open(attachHtmlReport, "rb") as attachment:
                part3 = MIMEBase("application", "octet-stream")
                part3.set_payload(attachment.read())
            encoders.encode_base64(part3)
            part3.add_header(
                "Content-Disposition",
                f"attachment; filename= {attachHtmlReport}",
            )
            message.attach(part3)
        except Exception as err:
            print(f"Exception! Failed to attach {attachHtmlReport} to mail - {err}")
            pass

    message.attach(part2)
    message.attach(part1)

    # Create secure connection with server and send email
    context = ssl.create_default_context()
    with smtplib.SMTP_SSL("smtp.gmail.com", 465, context=context) as server:
        server.login(sender_email, sender_apptoken)
        print("Logged in successfully")
        server.sendmail(
            sender_email, receiver_email, message.as_string()
        )
        print(f"Notified Successfully from {sender_email} to {receiver_email}")


def NotifyToUsers(resultPath):
    try:
        deployAllureReportToNetlify(resultPath)
        netlifyLink = get_netlify_report_link()
        send_automation_report(netlifyLink)
        return True
    except Exception as err:
        print(f"Error:{err}")
        return False
