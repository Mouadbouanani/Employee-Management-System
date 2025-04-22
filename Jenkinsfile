pipeline {
    agent any

    tools {
            maven 'Maven'
            jdk 'JDK'
        }

        environment {
            JAVA_HOME = "${tool 'JDK'}"
            PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
        }
    stages {

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }


    post {
        success {
            echo '🎉 Build terminé avec succès !'
        }
        failure {
            echo '💥 Le build a échoué.'
        }
    }
}
