#!groovy

def email_alert()
{
	emailext (
		subject: "[Automation Execution Report] [${env.JOB_NAME}] Build#${env.BUILD_NUMBER}:${currentBuild.result}",
		body:
		"""
		Hi All,

		    Below is the OS Automation Execution Report -

            Build#${env.BUILD_NUMBER} : ${currentBuild.result}
            Tags Selected   : ${env.TAGS_TO_TEST}
            PipeLine        : ${env.JOB_NAME}
            Build Number    : ${env.BUILD_NUMBER}
            Build URL       : ${env.BUILD_URL}console
            Pipeline Steps  : ${env.BUILD_URL}flowGraphTable
            Git changes     : ${env.BUILD_URL}changes

        Thanks
        Quantum OS Automation
         """,
		to: "${params.emailID}",
		from: "pythonsikho2023@gmail.com"
		)
}

properties([
	parameters([
        string(name: 'MODULE_TO_TEST', defaultValue: 'vmware', description: 'Module to be tested. Default value is vmware'),
        string(name: 'TAGS_TO_TEST', defaultValue: 'BuildCheck', description: 'Tags to be tested. Default value is BuildCheck'),
        string(name: 'emailID', defaultValue: '', description: 'Notify when execution complete. Provide receiver mail ID'),
        separator(name: "sep_1"),
        booleanParam(name: 'User_inputs_through_file_upload', description:"Or Fill the below form with required parameters"),
        separator(name: "sep_2"),

        //separator(name: "bootmode", sectionHeader: "bootmode"),
        //booleanParam(name: 'Secure_Boot_Mode'),

        // ************************** vCenter**************************************
        //separator(name: "vCenter", sectionHeader: "vCenter"),
        string( name: 'vCenterIP', defaultValue: '172.20.22.197'),
        string( name: 'vCenterUsername', defaultValue: 'Administrator@vsphere.local'),
        string(name: 'vCenterPassword', defaultValue: 'Admin!23'),
        string(name: 'vCenterVersion', defaultValue: '8.0'),

        // ************************** Host ****************************************
        string( name: 'HostIP', defaultValue: '172.20.99.102'),
        string( name: 'HostUsername', defaultValue: 'root'),
        string(name: 'HostPassword', defaultValue: 'Compaq@123'),
        string(name: 'HostPort', defaultValue: '22'),
        string(name: 'HostClusterName', defaultValue: 'AutomatedCluster'),
        string(name: 'HostDatacenterLocation', defaultValue: 'Datacenters'),
        string(name: 'HostPortDatacenterName', defaultValue: 'AutomatedDC'),
        string(name: 'HostDatastore', defaultValue: 'datastore1'),
        string(name: 'HostNetworkAdapter', defaultValue: 'vmnic1'),
        string(name: 'HostHostProfile', defaultValue: 'AutomatedHP'),
        string(name: 'HostHostProfile_Description', defaultValue: 'Automated Host Profile'),

        // ************************** VM ****************************************
        string( name: 'VMLocation', defaultValue: 'H:/automation_resourcefiles/vms/w2k19-8b.ova'),
        string(name: 'VMName', defaultValue: 'WindowsVM'),
        string(name: 'VMusername', defaultValue: 'root'),
        string(name: 'VMpassword', defaultValue: 'Compaq@123'),
        string(name: 'VMdatastore1', defaultValue: 'datastore1'),
        string(name: 'VMdatastore2', defaultValue: 'datastore2'),
        string(name: 'VMguestos', defaultValue: 'windows'),
        string(name: 'VM2Name', defaultValue: 'Rhel'),
        string(name: 'VM2Location', defaultValue: 'H:/automation_resourcefiles/vms/RHEL8.6.OVA'),
        string(name: 'VM2username', defaultValue: 'root'),
        string(name: 'VM2password', defaultValue: 'Compaq@123'),
        string(name: 'VM2guestos', defaultValue: 'rhel'),


        // ************************** NFS ****************************************
        string( name: 'NFSDatastoreName', defaultValue: 'datastore_iscsi'),
        string( name: 'NFSNFSPath', defaultValue: '/nfs'),
        string(name: 'NFSNFSHostip', defaultValue: '172.20.1.151'),

        // ************************** iSCSI ****************************************
        string( name: 'iSCSIDatastoreName', defaultValue: 'iSCS_shared_ds'),
        string( name: 'iSCSIPath', defaultValue: '/iSCSI'),
        string(name: 'iSCSIHostip', defaultValue: '172.20.2.156'),
        string(name: 'msa_ip', defaultValue: '172.20.2.150'),
        string(name: 'msa_user', defaultValue: 'manage'),
        string(name: 'msa_pass', defaultValue: 'Compaq@123'),
        string(name: 'msa_ports', defaultValue: '3,4'),

        // ************************** SharedDatastore ****************************************
        string( name: 'SharedDatastoreName', defaultValue: 'isCSi1'),
        string( name: 'SharedDatastoreDatastoreName', defaultValue: 'NFS'),
        string(name: 'SharedDatastorePathToVMDK', defaultValue: '/akshitadpm/test1'),

        // ************************** VD_Switch ****************************************
        string( name: 'VD_SwitchName', defaultValue: 'vSwitchHP'),
        string( name: 'VD_SwitchPortGroup', defaultValue: 'vPortGroupHP'),

        // ************************** DepotInfo ****************************************
        string( name: 'DepotInfoDepot_name', defaultValue: 'VMware-ESXi-8.0.1-21006797-HPE-801.0.0.11.2.1.5-Mar2023-depot.zip'),
        string( name: 'DepotInfoImage_name', defaultValue: 'HPE-Custom-AddOn_801.0.0.11.2.1-5'),
        string( name: 'DepotInfoLocalpath', defaultValue: 'H:/automation_resourcefiles/OS/8.0U1/Build5/VMware-ESXi-8.0.1-21006797-HPE-801.0.0.11.2.1.5-Mar2023-depot.zip'),
        string( name: 'DepotInfoRemotepath', defaultValue: '/vmfs/volumes/datastore1'),

        // ************************** Source_VM ****************************************
        string( name: 'Source_VM_Name', defaultValue: 'Windows'),
        string( name: 'Source_VM_OVAFile', defaultValue: 'H:/automation_resourcefiles/vms/w2k19-8b.ova'),

        // ************************** Destination_VM ****************************************
        string( name: 'Destination_VMName', defaultValue: 'TestWinVM1Final'),
        string( name: 'Destination_VMDatastore', defaultValue: 'datastore1 (1)'),
        string( name: 'Destination_VMHost', defaultValue: '172.20.17.140'),

        // ************************** Template ****************************************
        string( name: 'TemplateName', defaultValue: 'WinVMTemplate'),
        string( name: 'TemplateLocation', defaultValue: 'vmTemplate'),
        string( name: 'TemplateDatastore', defaultValue: 'datastore1 (1)'),

        // ************************** Hosts(Multi) ****************************************
        //Host1
        string( name: 'Hostsvm_count', defaultValue: '4'),
        string( name: 'Host1IP', defaultValue: '172.20.11.143'),
        string( name: 'Host1Username', defaultValue: 'root'),
        string( name: 'Host1Password', defaultValue: 'Admin!23'),
        string( name: 'Host1Port', defaultValue: '22'),
        string( name: 'Host1datastore_name', defaultValue: 'datastore1'),
        string( name: 'Host1BMCIP', defaultValue: '172.20.18.164'),
        string( name: 'Host1BMCMACAddr', defaultValue: '08:F1:EA:82:52:F8'),
        string( name: 'Host1BMCUsername', defaultValue: 'admin'),
        string( name: 'Host1BMCPassword', defaultValue: 'admin123'),
        string( name: 'Host1NetworkAdapter', defaultValue: 'vmnic1'),
        string( name: 'Host1Memory', defaultValue: '256'),
        string( name: 'Host1hdd', defaultValue: '2868'),
        string( name: 'Host1vm_name', defaultValue: 'VM11'),
         string( name: 'Host1Virtual_SwitchName', defaultValue: 'vswitch6'),
         string( name: 'Host1Virtual_SwitchNetwork_label', defaultValue: 'VMotion'),
         string( name: 'Host1Virtual_SwitchPortgroup', defaultValue: 'DPMPort'),

         string( name: 'Host1VMsvms_count', defaultValue: '3'),
         string( name: 'Host1VMsvm1Name', defaultValue: '"Win1","Win2","Win3"', description: 'Provide VM name separated by comma like "Rhel1","Rhel2","Rhel3"'),
         string( name: 'Host1VMsguestos', defaultValue: 'Windows'),


        //Host2
        string( name: 'Host2IP', defaultValue: '172.20.17.47'),
        string( name: 'Host2Username', defaultValue: 'root'),
        string( name: 'Host2Password', defaultValue: 'Compaq@123'),
        string( name: 'Host2Port', defaultValue: '22'),
        string( name: 'Host2datastore_name', defaultValue: 'datastore2'),
        string( name: 'Host2BMCIP', defaultValue: '172.20.12.130'),
        string( name: 'Host2BMCMACAddr', defaultValue: '94:40:C9:32:5B:FE'),
        string( name: 'Host2BMCUsername', defaultValue: 'admin'),
        string( name: 'Host2BMCPassword', defaultValue: 'admin123'),
        string( name: 'Host2NetworkAdapter', defaultValue: 'vmnic1'),
        string( name: 'Host2Memory', defaultValue: '256'),
        string( name: 'Host2hdd', defaultValue: '3584'),
         string( name: 'Host2Virtual_SwitchName', defaultValue: 'vswitch4'),
         string( name: 'Host2Virtual_SwitchNetwork_label', defaultValue: 'VMotion'),
         string( name: 'Host2Virtual_SwitchPortgroup', defaultValue: 'DPMPort'),

         string( name: 'Host2VMsvms_count', defaultValue: '3'),
         string( name: 'Host2VMsvm1Name', defaultValue: '"Win21","Win22"', description: 'Provide VM name separated by comma like "Rhel1","Rhel2","Rhel3"'),
         string( name: 'Host2VMsguestos', defaultValue: 'Windows'),


        // ************************** TFTPServer ****************************************
        string( name: 'TFTPServerIP', defaultValue: '172.20.98.168'),
        string( name: 'TFTPServerUsername', defaultValue: 'Administrator'),
        string( name: 'TFTPServerPassword', defaultValue: 'Compaq@123'),

        // ************************** Failover ****************************************
        string( name: 'FailoverHost1', defaultValue: '172.20.4.33'),
        string( name: 'FailoverHost2', defaultValue: '172.20.12.33'),


        // ************************** iSCSi_Switch ****************************************
        string( name: 'iSCSi_SwitchName', defaultValue: 'iscsivSwitch'),
        string( name: 'iSCSi_SwitchPortgroup1', defaultValue: 'iSCSI-PG1'),
        string( name: 'iSCSi_SwitchPortgroup2', defaultValue: 'iSCSI-PG2'),


         // ************************** ILO ****************************************
        string( name: 'ILOIP', defaultValue: '172.20.100.6'),
        string( name: 'ILOUsername', defaultValue: 'admin'),
        string( name: 'ILOPassword', defaultValue: 'admin123'),
        string( name: 'ILOOneTimeBootOption', defaultValue: 'OCP Slot 10 Port 2 : Mellanox Network Adapter - B8:83:03:A3:3E:7D (PXE IPv4)'),


        // ************************** AutomationResourceFiles ****************************************
        string( name: 'ResourcePath', defaultValue: 'H:/automation_resourcefiles'),
        string( name: 'ResourceRelease_Note', defaultValue: 'H:/automation_resourcefiles/release_notes/70/releasenotes.txt'),
        string( name: 'ResourceImageResourcePath', defaultValue: 'H:/automation_resourcefiles/imagefiles'),
        string( name: 'ResourceVibList', defaultValue: 'H:/automation_resourcefiles/viblist'),
        string( name: 'ResourceImageDepotFile', defaultValue: 'H:/automation_resourcefiles/OS/8.0U1/Build5/VMware-ESXi-8.0.1-21006797-HPE-801.0.0.11.2.1.5-Mar2023-depot.zip'),
        string( name: 'ResourceUtility_Source_Path', defaultValue: 'H:/automation_resourcefiles/Utility_Builds/HPE-Utility-Component_10.8.0.700-22_18497760.zip'),
        string( name: 'ResourceDepotLocalpath', defaultValue: 'H:/automation_resourcefiles/OS/8.0U1/Build5/VMware-ESXi-8.0.1-21006797-HPE-801.0.0.11.2.1.5-Mar2023-depot.zip'),
        string( name: 'Resourceova_fileRhelLocation', defaultValue: 'H:/automation_resourcefiles/vms/RHEL8.6.OVA'),
        string( name: 'Resourceova_fileWindowsLocation', defaultValue: 'H:/automation_resourcefiles/vms/w2k19-8b.ova'),
        string( name: 'Resourcestressfile_path', defaultValue: 'H:/automation_resourcefiles/stress/Xorsystwindow/xorsyst5.4.6_windows_x64.zip'),
        string( name: 'Resourcestressxorsyst_license', defaultValue: 'H:/automation_resourcefiles/stress/Xorsystwindow/xorsyst.license'),
        string( name: 'ResourceIso_Image', defaultValue: 'VMware-ESXi-8.0.1-21006797-HPE-801.0.0.11.2.1.5-Mar2023.iso'),
        string( name: 'Resourcestress_time_min', defaultValue: '15'),
	])
])


try {
stage('Git Checkout') {
    println "We are in checkout stage"
    def new_exclude_patterns = [[pattern: "**", type: 'INCLUDE']]
    cleanWs deleteDirs: true, skipWhenFailed: true, patterns: new_exclude_patterns
    checkout scm
    println "[Stage1] Git Checkout - Successfully"
    } // End Of Stage block
catch (Exception e) {
    println(e.toString())
    currentBuild.result = 'ABORTED'
    error("Stopping ...")
} // End of Try Catch block

/// [Stage-2] The below stage is to have the template updated before executing like IP address / username to execute tests ..
    try {

        stage("Preparing ENV") {
           println "Updating user inputs.Please wait a while."

		   def http_server_ip =  "https://15.118.51.44/os_qual_cert/AutomationResourceFiles/Vmware/"
		   def ResourcePath = "${params.ResourcePath}".replaceAll(http_server_ip, "/var/www/html/")
		   def ResourceRelease_Note = "${params.ResourceRelease_Note}".replaceAll(http_server_ip, "/var/www/html/")
		   def ResourceImageResourcePath = "${params.ResourceImageResourcePath}".replaceAll(http_server_ip, "/var/www/html/")
		   def ResourceVibList = "${params.ResourceVibList}".replaceAll(http_server_ip, "/var/www/html/")
		   def ResourceImageDepotFile = "${params.ResourceImageDepotFile}".replaceAll(http_server_ip, "/var/www/html/")
		   def ResourceUtility_Source_Path = "${params.ResourceUtility_Source_Path}".replaceAll(http_server_ip, "/var/www/html/")
		   def ResourceDepotLocalpath = "${params.ResourceDepotLocalpath}".replaceAll(http_server_ip, "/var/www/html/")
		   def Resourceova_fileRhelLocation = "${params.Resourceova_fileRhelLocation}".replaceAll(http_server_ip, "/var/www/html/")
		   def Resourceova_fileWindowsLocation = "${params.Resourceova_fileWindowsLocation}".replaceAll(http_server_ip, "/var/www/html/")
		   def Resourcestressfile_path = "${params.Resourcestressfile_path}".replaceAll(http_server_ip, "/var/www/html/")
		   def Resourcestressxorsyst_license = "${params.Resourcestressxorsyst_license}".replaceAll(http_server_ip, "/var/www/html/")
		   def ResourceIso_Image = "${params.ResourceIso_Image}".replaceAll(http_server_ip, "/var/www/html/")

		   def data = """
		{
              "version": 5.0,
              "vCenter": {
                "IP": "${params.vCenterIP}",
                "Username": "${params.vCenterUsername}",
                "Password": "${params.vCenterPassword}",
                "Version": "${params.vCenterVersion}"
              },
              "Host": {
                "IP": "${params.HostIP}",
                "Username": "${params.HostUsername}",
                "Password": "${params.HostPassword}",
                "Port": "${params.HostPort}",
                "ClusterName": "${params.HostClusterName}",
                "DatacenterLocation": "${params.HostDatacenterLocation}",
                "DatacenterName": "${params.HostPortDatacenterName}",
                "Datastore": "${params.HostDatastore}",
                "NetworkAdapter": "${params.HostNetworkAdapter}",
                "HostProfile": "${params.HostHostProfile}",
                "HostProfile_Description": "${params.HostHostProfile_Description}"
              },
              "VM": {
                "Location": "${params.VMLocation}",
                "Name": "${params.VMName}",
                "username": "${params.VMusername}",
                "password": "${params.VMpassword}",
                "datastore1": "${params.VMdatastore1}",
                "datastore2": "${params.VMdatastore2}",
                "guestos": "${params.VMguestos}",
                "VM2": {
                  "Name": "${params.VM2Name}",
                  "Location": "${params.VM2Location}",
                  "username": "${params.VM2username}",
                  "password": "${params.VM2password}",
                  "guestos": "${params.VM2guestos}"
                }
              },
              "NFS": {
                "DatastoreName": "${params.NFSDatastoreName}",
                "NFSPath": "${params.NFSNFSPath}",
                "NFSHostip": "${params.NFSNFSHostip}"
              },
              "iSCSI": {
                "DatastoreName": "${params.iSCSIDatastoreName}",
                "iSCSIPath": "${params.iSCSIPath}",
                "iSCSIHostip": "${params.iSCSIHostip}",
                "msa_ip": "${params.msa_ip}",
                "msa_user": "${params.msa_user}",
                "msa_pass": "${params.msa_pass}",
                "msa_ports": "${params.msa_ports}"
              },
              "SharedDatastore": {
                "Name": "${params.SharedDatastoreName}",
                "DatastoreName": "${params.SharedDatastoreDatastoreName}",
                "PathToVMDK": "${params.SharedDatastorePathToVMDK}"
              },
              "VD_Switch": {
                "Name": "${params.VD_SwitchName}",
                "PortGroup": "${params.VD_SwitchPortGroup}"
              },
              "DepotInfo": {
                "Depot_name": "${params.DepotInfoDepot_name}",
                "Image_name": "${params.DepotInfoImage_name}",
                "Localpath": "${params.DepotInfoLocalpath}",
                "Remotepath": "${params.DepotInfoRemotepath}"
              },
              "Source_VM": {
                "Name": "${params.Source_VM_Name}",
                "OVAFile": "${params.Source_VM_OVAFile}"
              },
              "Destination_VM": {
                "Name": "${params.Destination_VMName}",
                "Datastore": "${params.Destination_VMDatastore}",
                "Host": "${params.Destination_VMHost}"
              },
              "Template": {
                "Name": "${params.TemplateName}",
                "Location": "${params.TemplateLocation}",
                "Datastore": "${params.TemplateDatastore}"
              },
              "Hosts": {
                "vm_count": ${params.Hostsvm_count},
                "Host1": {
                  "IP": "${params.Host1IP}",
                  "Username": "${params.Host1Username}",
                  "Password": "${params.Host1Password}",
                  "Port": "${params.Host1Port}",
                  "datastore_name": "${params.Host1datastore_name}",
                  "BMCIP": "${params.Host1BMCIP}",
                  "BMCMACAddr": "${params.Host1BMCMACAddr}",
                  "BMCUsername": "${params.Host1BMCUsername}",
                  "BMCPassword": "${params.Host1BMCPassword}",
                  "NetworkAdapter": "${params.Host1NetworkAdapter}",
                  "memory": ${params.Host1Memory},
                  "hdd": ${params.Host1hdd},
                  "vm_name": "${params.Host1vm_name}",
                  "Virtual_Switch": {
                    "Name": "${params.Host1Virtual_SwitchName}",
                    "Network_label": "${params.Host1Virtual_SwitchNetwork_label}",
                    "Portgroup": "${params.Host1Virtual_SwitchPortgroup}"
                  },
                  "VMs": {
                    "vms_count": ${params.Host1VMsvms_count},
                    "vm1": {
                      "Name": [${params.Host1VMsvm1Name}],
                      "guestos": "${params.Host1VMsguestos}"
                    }
                  }
                },

                "Host2": {
                   "IP": "${params.Host2IP}",
                  "Username": "${params.Host2Username}",
                  "Password": "${params.Host2Password}",
                  "Port": "${params.Host2Port}",
                  "datastore_name": "${params.Host2datastore_name}",
                  "BMCIP": "${params.Host2BMCIP}",
                  "BMCMACAddr": "${params.Host2BMCMACAddr}",
                  "BMCUsername": "${params.Host2BMCUsername}",
                  "BMCPassword": "${params.Host2BMCPassword}",
                  "NetworkAdapter": "${params.Host2NetworkAdapter}",
                  "memory": ${params.Host2Memory},
                  "hdd": ${params.Host2hdd},
                  "Virtual_Switch": {
                    "Name": "${params.Host2Virtual_SwitchName}",
                    "Network_label": "${params.Host2Virtual_SwitchNetwork_label}",
                    "Portgroup": "${params.Host2Virtual_SwitchPortgroup}"
                  },
                  "VMs": {
                    "vms_count": ${params.Host2VMsvms_count},
                    "vm1": {
                      "Name": [${params.Host2VMsvm1Name}],
                      "guestos": "${params.Host2VMsguestos}"
                    }
                  }
                }
              },
              "TFTPServer": {
                "IP": "${params.TFTPServerIP}",
                "Username": "${params.TFTPServerUsername}",
                "Password": "${params.TFTPServerPassword}"
              },
              "Failover": {
                "Host1": "${params.FailoverHost1}",
                "Host2": "${params.FailoverHost2}"
              },
              "iSCSi_Switch": {
                "Name": "${params.iSCSi_SwitchName}",
                "Portgroup1": "${params.iSCSi_SwitchPortgroup1}",
                "Portgroup2": "${params.iSCSi_SwitchPortgroup2}"
              },
              "ILO": {
                "IP": "${params.ILOIP}",
                "Username": "${params.ILOUsername}",
                "Password": "${params.ILOPassword}",
                "OneTimeBootOption": "${params.ILOOneTimeBootOption}"
              },
              "AutomationResourceFiles": {
                "Path": "${params.ResourcePath}",
                "Release_Note": "${params.ResourceRelease_Note}",
                "ImageResourcePath": "${params.ResourceImageResourcePath}",
                "VibList": "${params.ResourceVibList}",
                "ImageDepotFile": "${params.ResourceImageDepotFile}",
                "Utility_Source_Path": "${params.ResourceUtility_Source_Path}",
                "DepotLocalpath": "${params.ResourceDepotLocalpath}",
                "ova_file": {
                  "Rhel": {
                    "Location": "${params.Resourceova_fileRhelLocation}"
                  },
                  "Windows": {
                    "Location": "${params.Resourceova_fileWindowsLocation}"
                  }
                },
                "stress": {
                  "file_path": "${params.Resourcestressfile_path}",
                  "xorsyst_license": "${params.Resourcestressxorsyst_license}",
                  "stress_time_min": ${params.Resourcestress_time_min}
                },
                "Iso_Image": "${params.ResourceIso_Image}"
              }
             }"""

			 //Works on Linux machine
			//def file = new File(""$WORKSPACE\\user_input.json"")
			//file.write(data)

             if(params.User_inputs_through_file_upload) {
                echo "Yes equal - User opted to upload the user input file."
                fileBase64 = input message: 'Please provide a file', parameters: [base64File('file')]
                withEnv(["fileBase64=$fileBase64"]) {
                powershell '''
                $fileBase64=Get-ChildItem Env: | Where-Object { $_.Name -eq "fileBase64" } | Select-Object -ExpandProperty Value
                $base64String = $fileBase64
                $bytes = [System.Convert]::FromBase64String($base64String)
                $string = [System.Text.Encoding]::UTF8.GetString($bytes)
                Write-Output $string
                Write-Output $string > user_input.json
                '''
                }
            } else {
                echo "Not equal - User opted to to fill the form"
                println(data)
			    writeFile (file: "$WORKSPACE\\user_input.json", text: "${data}")
            }

        }
    } catch (Exception e) {
        println(e.toString())
        currentBuild.result = 'ABORTED'
        error("Stopping ...")

    }

/// [Stage-3] The stage is to execute the tests based on the parameters provided.
    try {

        stage('build') {
            def MODULE = "${params.MODULE_TO_TEST}"
            def TAGS = "${params.TAGS_TO_TEST}"

            //For Windows Slave
            script {
                bat """
                set PYTHONPATH=C:\\Users\\Administrator\\AppData\\Local\\Programs\\Python\\Python310\\Scripts
                C:\\Users\\Administrator\\AppData\\Local\\Programs\\Python\\Python310\\python.exe --version
                C:\\Users\\Administrator\\AppData\\Local\\Programs\\Python\\Python310\\python.exe -u runner.py --module ${MODULE} --tags "@${TAGS}" --logcollect True --job_id "bdd_automation_${env.JOB_NAME}_${currentBuild.number}" --jenkins True --slave True
                 """
            }

//             For Linux SLave
//             sh """
//             set -x
//             python --version
// 		       python -u runner.py --module ${MODULE} --tags "@${TAGS}" --logcollect True --job_id "bdd_automation_${currentBuild.number}" --jenkins True
//             """
	        println "[Stage3] Test Execution - Successfully"
        }

    } catch (Exception e) {
        println(e.toString())
        currentBuild.result = 'ABORTED'
        error("Stopping ...")
    }


/// [Stage-4] The stage is to generate and publish the allure report for Jenkins.
    try {
        stage("Report Generation") {
            echo "Generating Allure Report ..."
            allure report: 'allure_reports', results: [[path: 'allure_results']]
            echo "Report generation done."
 	    println "[Stage4] Report Publish - Successfully"
        }
    } catch (Exception e) {
        println(e.toString())
        currentBuild.result = 'ABORTED'
        error("Stopping ...")
    }

    /// [Stage-5] The stage is to collect executions logs for future debugging
    try {
        stage("Log Collection") {
	    println "[Stage5] Log Published - Successfully"
            echo "Logs Uploaded at below link-"
            echo "https://15.118.51.44/os_qual_cert/logs/bdd_automation_${env.JOB_NAME}_${currentBuild.number}.zip"
            email_alert()
        }
    } catch (Exception e) {
        println(e.toString())
        currentBuild.result = 'ABORTED'
        error("Stopping ...")
    }
}
