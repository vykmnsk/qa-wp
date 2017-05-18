#!/usr/bin/env groovy
@Library('potatocannon-global')
import au.com.tabcorp.potatocannon.*

pipeline {

    agent {
        label 'java18'
    }

    stages {
        stage('checkout & install dependencies') {
            steps {
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

        stage('redbook smoke test : create event') {
            steps {
                sh 'TEST_TAGS="-t @smoke -t @ui -t @redbook -t ~@luxbet -t ~@luxbet-mobile" docker-compose up -d  --force-recreate --build'
                sh 'exit $(docker wait testservice)'
            }

            post {
                always {
                    sh 'docker-compose logs testservice'
                }
            }
        }

        stage('common: create customer ui tests') {
            steps {
                sh 'TEST_TAGS="-t @redbook -t @ui -t @create-customer -t @cash-deposit" docker-compose up -d  --force-recreate --build'
                sh 'exit $(docker wait testservice)'
            }

            post {
                always {
                    sh 'docker-compose logs testservice'
                }
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