#!/usr/bin/env groovy

node('java18') {
    wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'XTerm', 'defaultFg': 1, 'defaultBg': 2]) {

        try {
            stage('Test') {
                checkout scm
                sh 'mvn test'
            }

        } catch (e) {
            println "THE BUILD FAILED"
            throw e
        }

    }
}