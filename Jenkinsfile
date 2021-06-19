pipeline {
	agent any
	environment {
		NEXUS = 'ermakovis'
		RELEASE_TYPE = getReleaseType(env.BRANCH_NAME)
		DB_CREDENTIALS = credentials('db_cred')
		REGISTRY_CREDENTIALS = credentials('registry-cred')
		VERSION = getNextVersion(RELEASE_TYPE)
	}
	stages {
        stage('Env') {
            steps {
                echo "${VERSION}"
                echo "${REGISTRY_CREDENTIALS_USR}"
            }
        }
        stage('Build') {
            steps {
                withMaven(maven: 'maven 3.8.1') {
                    sh 'mvn clean package'
                }
            }
        }
        stage('Push version'){
            steps {
                    sh "git tag ${VERSION}"
                    sh "git push --tags --no-verify"
            }
        }
    }
}

static def getReleaseType(branchName) {
    if(branchName == "develop" || branchName.startsWith("feature/")) {
        return "SNAPSHOT";
    } else if(branchName == "main" || branchName.startsWith("release/")){
        return "RELEASE";
    }
    else {
        throw new Exception("Invalid branch - ${branchName}")
    }
}

def getNextVersion(scope) {
    def latestVersion = sh returnStdout: true, script: 'git describe --tags "$(git rev-list --tags=*.*.* --max-count=1 2> /dev/null)" 2> /dev/null || echo 1.0.0'
    def (major, minor, patch) = latestVersion.tokenize('.').collect { it.toInteger() }
    def nextVersion
	    switch (scope) {
	        case 'major':
	            nextVersion = "${major + 1}.0.0"
	            break
	        case 'RELEASE':
	            nextVersion = "${major}.${minor + 1}.0"
	            break
	        case 'SNAPSHOT':
	            nextVersion = "${major}.${minor}.${patch + 1}"
	            break
	    }
    nextVersion
}