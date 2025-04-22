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
                        bat 'mvn verify -Dgroups=functional'
                    }
                }
                stage('Performance Testing') {
                    environment {
                        PATH = "C:\Tools\apache-jmeter-5.6.3\bin;${env.PATH}"
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
                        bat 'mvn findbugs:findbugs'
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
                stage('Artifactory Upload') {
                    steps {
                        echo 'Upload vers Artifactory (simulation)'
                        bat 'mvn deploy'
                    }
                }
                stage('Nexus Upload') {
                    steps {
                        echo 'Upload vers Nexus (simulation)'
                        bat 'mvn deploy'
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
