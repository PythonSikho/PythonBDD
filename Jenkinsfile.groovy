#!groovy

try {
 stage("Git Checkout") {
    println "We are in checkout stage"
    def new_exclude_patterns = [[pattern: "**", type: 'INCLUDE']]
    cleanWs deleteDirs: true, skipWhenFailed: true, patterns: new_exclude_patterns
    checkout scm
    println "[Stage1] Git Checkout - Successfully"
    }
} catch (Exception e) {
    println(e.toString())
    currentBuild.result = 'ABORTED'
    error("Stopping ...")
}
