pipeline {
    agent any
//    parameters {
//        string(defaultValue: '', description: 'Użytkownik do ssh z uprawnieniami do modyfikacji plików na serwerze produkcyjnym.', name: 'USER')
//        string(defaultValue: '', description: 'Adres serwera produkcyjnego.', name: 'SERVER')
//        string(defaultValue: '/opt/watson', description: 'Ścieżka do katalogu z aplikacją.', name: 'PATH')
//    }
    stages {
//        stage('Set release version') {
//            steps {
//                sh './mvnw versions:set versions:commit -DremoveSnapshot'
//            }
//        }
//        stage('Build release webapp') {
//            steps {
//                sh ' ./mvnw clean install'
//                sh 'echo $(./mvnw help:evaluate -Dexpression=project.version 2>/dev/null | grep -v "\\[" | sed -n 2p) > version.txt'
//                sh 'sed -i"" s/RELEASE_VERSION/$(cat version.txt)/g CHANGELOG.md'
//                archiveArtifacts '**/watson-web*.jar'
//            }
//            post {
//                always {
//                    junit '**/target/surefire-reports/**/*.xml'
//                }
//            }
//        }
//        stage('Commit release version') {
//            steps {
//                sh 'git add .'
//                sh 'git commit -m "Release: $(cat version.txt)"'
//                sh 'git tag -f "v$(cat version.txt)"'
//            }
//        }
//        stage('Prepare next snapshot') {
//            steps {
//                sh './mvnw build-helper:parse-version versions:set versions:commit -DnewVersion="\\${parsedVersion.majorVersion}.\\${parsedVersion.minorVersion}.\\${parsedVersion.nextIncrementalVersion}-SNAPSHOT"'
//                sh 'echo $(./mvnw help:evaluate -Dexpression=project.version 2>/dev/null | grep -v "\\[" | sed -n 2p) > version.txt'
//                sh 'git add .'
//                sh 'git commit -m "Preparing next snapshot: $(cat version.txt)"'
//            }
//        }
//        stage('Push to origin') {
//            steps {
//                sh 'git push --set-upstream origin master'
//                sh 'git push --tags origin'
//            }
//        }
        stage('Deploy to production') {
            steps {
                sh 'echo ${params.SERVER}'
            }
        }
    }
}
