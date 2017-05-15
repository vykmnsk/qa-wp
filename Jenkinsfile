#!/usr/bin/env groovy
@Library('potatocannon-global')
import au.com.tabcorp.potatocannon.*

pipeline {

    agent {
        label 'java18'
    }

    stages {
        stage('initialize') {
            steps {
                checkout scm
            }
        }

        /****************
         *  sh 'curl -vvv  http://wp1-e-wploa-1k3cf5zurx16s-1221313599.eu-west-1.elb.amazonaws.com/admin/frames.php'
         * Just to check if the url was reachable from the jenkins worker.
         ****************/

        stage('install maven dependencies') {
            steps {
                sh 'TEST_TAGS="install-dependencies" docker-compose up -d  --force-recreate --build'
                sh 'exit $(docker wait testservice)'
            }

            post {
                always{
                    sh 'docker-compose logs testservice'
                }
            }
        }

        stage('login tests') {
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

        stage('create customer ui tests') {
            steps {
                sh 'TEST_TAGS="-t @redbook -t @ui -t @create-customer -t ~@credit-card" docker-compose up -d  --force-recreate --build'
                sh 'exit $(docker wait testservice)'
            }

            post {
                always {
                    sh 'docker-compose logs testservice'
                }
            }
        }

        stage('create customer api tests') {
            steps {
                sh 'TEST_TAGS="-t @redbook -t @api -t @create-customer -t ~@credit-card" docker-compose up -d  --force-recreate --build'
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