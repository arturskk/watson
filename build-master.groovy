pipeline {
    agent any
    stages {
        stage('Set release version') {
            steps {
                sh './mvnw versions:set versions:commit -DremoveSnapshot'
            }
        }
        stage('Build release modules') {
            parallel {
                stage('Build frontend') {
                    steps {
                        sh ' ./mvnw clean install -pl watson-frontend'
                    }
                }
                stage('Build rest') {
                    steps {
                        sh ' ./mvnw clean install -pl watson-rest -am'
                    }
                }
            }
        }
        stage('Build release webapp') {
            steps {
                sh ' ./mvnw clean install -pl watson-web'
                sh 'echo $(./mvnw help:evaluate -Dexpression=project.version 2>/dev/null | grep -v "\\[" | sed -n 2p) > version.txt'
                sh 'sed -i"" s/RELEASE_VERSION/$(cat version.txt)/g CHANGELOG.md'
                archiveArtifacts '**/watson-web*.jar'
            }
            post {
                always {
                    junit '**/target/surefire-reports/**/*.xml'
                }
            }
        }
        stage('Commit release version') {
            steps {
                sh 'git add .'
                sh 'git commit -m "Release: $(cat version.txt)"'
                sh 'git tag -f "v$(cat version.txt)"'
            }
        }
        stage('Prepare next snapshot') {
            steps {
                sh './mvnw build-helper:parse-version versions:set versions:commit -DnewVersion="\\${parsedVersion.majorVersion}.\\${parsedVersion.minorVersion}.\\${parsedVersion.nextIncrementalVersion}-SNAPSHOT"'
                sh 'echo $(./mvnw help:evaluate -Dexpression=project.version 2>/dev/null | grep -v "\\[" | sed -n 2p) > version.txt'
                sh 'git add .'
                sh 'git commit -m "Preparing next snapshot: $(cat version.txt)"'
            }
        }
        stage('Push to origin') {
            steps {
                sh 'git push --set-upstream origin master'
                sh 'git push --tags origin'
            }
        }
    }
}
