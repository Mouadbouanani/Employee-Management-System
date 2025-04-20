pipeline {
    agent any

    tools {
        maven 'Maven 3.8.6' // Doit correspondre au nom de l'outil Maven configuré dans Jenkins
        jdk 'JDK 17'         // Idem pour le JDK
    }

    environment {
        JAVA_HOME = "${tool 'JDK 17'}"
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/ton-utilisateur/ton-repo.git'
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
