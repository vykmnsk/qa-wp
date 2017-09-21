#!/usr/bin/env groovy
@Library('potatocannon-global')
import au.com.tabcorp.potatocannon.*

def USERNAME = "qa-bot"
def ENCRYPTED_API_KEY = ""

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
                checkout scm
                sh 'ENV=src/env_files/php7_env  TEST_TAGS="install-dependencies" docker-compose up -d  --force-recreate --build'
                sh 'exit $(docker wait testservice)'
            }
        }

        /****************
         *  sh 'curl -vvv  http://wp1-e-wploa-1k3cf5zurx16s-1221313599.eu-west-1.elb.amazonaws.com/admin/frames.php'
         * Just to check if the url was reachable from the jenkins worker.
         ****************/

        stage('gatekeeper tests') {
            steps {
                parallel(
                        "login tests": {
                            sh 'ENV=src/env_files/php7_env TEST_TAGS="-t @login" docker-compose up -d  --force-recreate --build'
                            sh 'exit $(docker wait testservice)'
                        },
                        "loss limit tests": {
                            sh 'docker build -t "wagerplayer-red:io" .'
                            sh 'docker run -e TEST_TAGS="-t @loss-limit" --env-file=src/env_files/php7_env wagerplayer-red:io --rm=false'
                        }
                )
            }

            post {
                always {
                    sh 'ENV=src/env_files/php7_env docker-compose logs testservice'
                }
            }
        }

        stage('redbook smoke tests') {
            steps {
                sh 'ENV=src/env_files/php7_env TEST_TAGS="-t @smoke -t ~@login -t ~@luxbet -t ~@luxbet-mobile" docker-compose up -d  --force-recreate --build'
                sh 'exit $(docker wait testservice)'
            }

            post {
                always {
                    sh 'ENV=src/env_files/php7_env docker-compose logs testservice'
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
                        repo = "redbook-docker-dev.artifacts.com"
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
            sh 'echo "To ensure conatiners have been brought down."'
            sh 'docker ps -a'
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