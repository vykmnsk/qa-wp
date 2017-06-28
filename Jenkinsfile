#!/usr/bin/env groovy
@Library('potatocannon-global')
import au.com.tabcorp.potatocannon.*

def USERNAME = "tabcorp-qa-bot"
def ENCRYPTED_API_KEY = "AQECAHhggPTequCEJZiWje2nomwraogaydeiw6VFPgL4Kmh9JQAAAKswgagGCSqGSIb3DQEHBqCBmjCBlwIBADCBkQYJKoZIhvcNAQcBMB4GCWCGSAFlAwQBLjARBAzW6c5WzBP2mJddsXICARCAZJmeAANoGnXwV0srjY3M3JKo03i6PwqZwsoCYxjoF9Nh7Oswzhueq2ktoetTqglOqeV2JuZjgsCxd1dfFzkyntdaXqwT/bPkP2ApeHAVH+KX4PtHMjlJzgb4sk4PJ7/z6QvdXbk="

pipeline {

    agent {
        label 'java18'
    }

    options {
        disableConcurrentBuilds()
        retry(1)
    }

    triggers {
        // Times needs to be specified in UTC
        // TODO enable cron for master branch only
        // cron('0 22 * * *')
        pollSCM('*/2 * * * *')
    }

    stages {
        stage('checkout & install dependencies') {
            steps {
                sh 'docker ps -a'
                sh 'docker-compose down --remove-orphans'
                checkout scm
                sh 'TEST_TAGS="install-dependencies" docker-compose up -d  --force-recreate --build'
                sh 'exit $(docker wait testservice)'
            }

            post {
                always {
                    sh 'docker-compose logs testservice'
                }
            }
        }

        /****************
         *  sh 'curl -vvv  http://wp1-e-wploa-1k3cf5zurx16s-1221313599.eu-west-1.elb.amazonaws.com/admin/frames.php'
         * Just to check if the url was reachable from the jenkins worker.
         ****************/

        stage('common : login tests') {
            steps {
                sh 'TEST_TAGS="-t @login" docker-compose up -d  --force-recreate --build'
                sh 'exit $(docker wait testservice)'
            }

            post {
                always {
                    sh 'docker-compose logs testservice'
                }
            }
        }

        stage('redbook smoke tests') {
            steps {
                sh 'TEST_TAGS="-t @smoke -t ~@luxbet -t ~@luxbet-mobile" docker-compose up -d  --force-recreate --build'
                sh 'exit $(docker wait testservice)'
            }

            post {
                always {
                    sh 'docker-compose logs testservice'
                }
            }
        }

        stage('publish') {

            when {
                branch 'master'
            }

            steps {

                script {
                    addDockerAuth {
                        username = USERNAME
                        password = ENCRYPTED_API_KEY
                        repo = "redbook-docker-dev.artifacts.tabdigital.com.au"
                    }
                }


                sh 'make create'
                sh 'make publish'
            }
        }
    }

    post {

        always {
            sh 'docker-compose down'
        }

        success {
            script {
                def changeLog = gitChangelog {}
                slackSuccess {
                    message = "Build succeeded :hooray: "
                    changelog = changeLog
                    channel = "#qa-wagerplayer-builds"
                }
            }
        }

        failure {
            script {
                def changeLog = gitChangelog {}
                slackFail {
                    message = "Build failed :sad_panda:"
                    changelog = changeLog
                    channel = "#qa-wagerplayer-builds"
                }
            }
        }
    }

}
