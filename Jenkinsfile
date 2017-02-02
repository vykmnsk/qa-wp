#!/usr/bin/env groovy

node('java18') {
    wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'css', 'defaultFg': 1, 'defaultBg': 2]) {

        try {
            stage('Build') {
                checkout scm
                sh 'mvn install -DskipTests'
            }

            stage('Test') {
                sh 'mvn test'
            }

        } catch (e) {
            println "THE BUILD FAILED"
            throw e
        }

    }
}