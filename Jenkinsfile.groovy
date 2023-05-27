#!groovy

properties([
	parameters([
        string(name: 'MODULE_TO_TEST', defaultValue: 'sauceLab', description: 'Module to be tested'),
        string(name: 'TAGS_TO_TEST', defaultValue: 'Login', description: 'Tags to be tested'),
	])
])

node("master") {
      println("Connecting to master....")
	  try {
        stage('Git Checkout') {
            println "We are in checkout stage"
            def new_exclude_patterns = [[pattern: "**", type: 'INCLUDE']]
            cleanWs deleteDirs: true, skipWhenFailed: true, patterns: new_exclude_patterns
            checkout scm
	        println "[Stage1] Git Checkout - Successfully"
        } // End Of Stage block
    } catch (Exception e) {
        println(e.toString())
        currentBuild.result = 'ABORTED'
        error("Stopping ...")
    } // End of Try Catch block


try {
        stage('Test Execution') {
            def MODULE = "${params.MODULE_TO_TEST}"
            def TAGS = "${params.TAGS_TO_TEST}"

            script {
                bat """
                set PYTHONPATH=C:\\Users\\fusio\\AppData\\Local\\Programs\\Python\\Python311\\Scripts
                C:\\Users\\fusio\\AppData\\Local\\Programs\\Python\\Python311\\python.exe --version
                C:\\Users\\fusio\\AppData\\Local\\Programs\\Python\\Python311\\python.exe -u runner.py --module ${MODULE} --tags "@${TAGS}"
                 """
            }
	        println "[Stage3] Test Execution - Successfully"
        }
} catch (Exception e) {
    println(e.toString())
    currentBuild.result = 'ABORTED'
    error("Stopping ...")
}

try {
    stage("Allure Report") {
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
}
