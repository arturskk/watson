pipeline {
    agent any
//    parameters {
//        string(defaultValue: '', description: 'Użytkownik do ssh z uprawnieniami do modyfikacji plików na serwerze produkcyjnym.', name: 'BUILD_PROD_USER')
//        string(defaultValue: '', description: 'Adres serwera produkcyjnego.', name: 'BUILD_PROD_SERVER')
//        string(defaultValue: '/opt/watson', description: 'Ścieżka do katalogu z aplikacją.', name: 'BUILD_PROD_APP_PATH')
//    }
    stages {
        stage('Set release version') {
            steps {
                sh './mvnw versions:set versions:commit -DremoveSnapshot'
            }
        }
        stage('Build release webapp') {
            steps {
                sh ' ./mvnw clean install'
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
        stage('Deploy to production') {
            steps {
                sh '''
BIN=$(cd watson-web/target/ && ls watson-web-*.jar)
scp watson-web/target/$BIN $BUILD_PROD_USER@$BUILD_PROD_SERVER:$BUILD_PROD_APP_PATH
ssh $BUILD_PROD_USER@$BUILD_PROD_SERVER << EOF
    systemctl stop watson
    sleep 10

    pushd $BUILD_PROD_APP_PATH
    mkdir -p backup
    cp watson.mv.db backup/watson.mv.db.$(date \'+%Y%m%d.%H%M%S\')

    echo "Old version:"
    ls -la watson.jar
    ln -fs $BIN watson.jar
    echo "New version:"
    ls -la watson.jar

    chown watson -R *

    systemctl start watson
    sleep 10
EOF
                '''
            }
        }
    }
}
