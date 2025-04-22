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
        stage('Checkout') {
            steps {
                git 'https://github.com/Mouadbouanani/Employee-Management-System.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Déploiement simulé ici...'
                // Exemple : sh './deploy.sh'
            }
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
