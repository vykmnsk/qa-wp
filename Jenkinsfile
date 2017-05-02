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
                /********
                 * Have named the selenium hub node in the docker-compose.yml
                 * so, can remove the hub by name as part of initial setup.
                 ********/
            }
        }

        stage('test') {
            steps {
                script {
                    /****************
                     *  sh 'curl -vvv  http://wp1-e-wploa-1k3cf5zurx16s-1221313599.eu-west-1.elb.amazonaws.com/admin/frames.php'
                     * Just to check if the url was reachable from the jenkins worker.
                     ****************/
                    sh 'docker-compose up -d --force-recreate --build'
                    sh 'exit $(docker wait testservice)'
                }
            }
        }
    }

    post {

        always {
            sh 'docker logs testservice'
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