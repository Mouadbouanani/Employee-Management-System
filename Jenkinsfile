pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK'
    }

    environment {
        JAVA_HOME = "${tool 'JDK'}"
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
        DOCKER_IMAGE = 'mouadbouanani/employee-management:latest'
    }

    stages {
        stage('SCM Checkout') {
            steps {
                echo 'Scrutation SCM'
                checkout scm
            }
        }

        stage('Build') {
            parallel {
                stage('Build with Maven') {
                    steps {
                        bat 'mvn clean compile'
                    }
                }
                stage('Build with Gradle') {
                    steps {
                        echo 'Simulated Gradle build (if applicable)'
                        // bat './gradlew build'
                    }
                }
            }
        }

        stage('Test') {
            parallel {
                stage('JUnit Tests') {
                    steps {
                        bat 'mvn test'
                    }
                    post {
                        always {
                            junit '**/target/surefire-reports/*.xml'
                        }
                    }
                }
                stage('Functional Testing') {
                    steps {
                        echo 'Simulated functional tests'
                        bat 'mvn verify'
                    }
                }
                stage('Performance Testing') {
                    environment {
                        PATH = "C:\\Tools\\apache-jmeter-5.6.3\\bin;${env.PATH}"
                    }
                    steps {
                        echo 'Lancement des tests de performance JMeter'
                        bat 'jmeter -n -t performance-tests/test.jmx -l results.jtl'
                    }
                }
            }
        }

        stage('Analyse du code') {
            parallel {
                stage('Checkstyle') {
                    steps {
                        bat 'mvn checkstyle:checkstyle'
                    }
                }
                stage('FindBugs') {
                    steps {
                        bat 'mvn com.github.spotbugs:spotbugs-maven-plugin:spotbugs'
                    }
                }
                stage('PMD') {
                    steps {
                        bat 'mvn pmd:pmd'
                    }
                }
            }
        }

        stage('JavaDoc') {
            steps {
                bat 'mvn javadoc:javadoc'
            }
        }

        stage('Packaging') {
            steps {
                bat 'mvn package'
            }
        }

        stage('Archiving') {
            parallel {
                stage('Nexus Upload') {
                    steps {
                        echo 'Upload vers Nexus (simulation)'
                        bat 'mvn deploy'
                    }
                }
            }
        }


        stage('Déploiement') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        bat """
                            echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin
                            docker build -t ${DOCKER_IMAGE} .
                            docker push ${DOCKER_IMAGE}
                        """
                    }

                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline terminé'
        }
        success {
            echo '🎉 Build terminé avec succès !'
        }
        failure {
            echo '💥 Le build a échoué.'
        }
    }
}
