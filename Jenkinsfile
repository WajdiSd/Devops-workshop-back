pipeline {
    agent any
    tools {
        maven 'maven3'
        jdk 'JDK11'
    }
    environment {
		DOCKERHUB_CREDENTIALS=credentials('Docker')
		
	}
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "M2_HOME=${M2_HOME}"
                '''
            }
        }
		
		stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh '''
					mvn sonar:sonar
					sleep 2
					'''
                }
            }
        }
        
		stage("Quality Gate") {
			steps {
			script{
			timeout(time: 1, unit: 'HOURS') { 
					def qg = waitForQualityGate() 
					if (qg.status != 'OK') {
						error "Pipeline aborted due to quality gate failure: ${qg.status}"
					}
			}
			}
			}
		}
		
		stage('Package') {
            steps {
                sh 'mvn -DskipTests clean package' 
            }
        }
		
		stage('Publish to Nexus Repository Manager') {
            steps {
                script {
					nexusArtifactUploader artifacts: [[artifactId: 'tpAchatProject', classifier: '', file: 'target/tpAchatProject-1.0.jar', type: 'jar']], credentialsId: 'nexus', groupId: 'com.esprit.examen', nexusUrl: '192.168.31.203:8081', nexusVersion: 'nexus3', protocol: 'http', repository: 'maven-snapshots', version: '1.0.0-SNAPSHOT'
				}
            }
            
        }
		
        stage('Build Docker') {

			steps {
				sh 'docker build -t wajdisd/springbootapp:1.0 .'
			}
		}
        stage('Login') {

			steps {

                sh ' docker login -u $DOCKERHUB_CREDENTIALS_USR -p $DOCKERHUB_CREDENTIALS_PSW'                		
	            echo 'Login Completed'  
			}
		}

		stage('Push') {

			steps {
				sh 'docker push wajdisd/springbootapp:1.0'
			}
		}
        
    }
    post {
    failure {
            emailext body: '${DEFAULT_CONTENT}', recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']], subject: '${DEFAULT_SUBJECT}',
            to: '${DEFAULT_RECIPIENTS}'
    }/*
    always {
			sh 'docker logout'
		}*/
  }
}