pipeline {
    agent any
    stages {
        stage('Clone repo') {
            steps {
                checkout(
                        [
                                $class: 'GitSCM',
                                branches: [[name: '*/master']],
                                browser: [$class: 'GithubWeb', repoUrl: ''],
                                doGenerateSubmoduleConfigurations: false,
                                extensions: [
                                        [$class: 'UserExclusion', excludedUsers: 'jenkins@lab.home'],
                                        [$class: 'UserExclusion', excludedUsers: 'jenkins'],
                                        [$class: 'LocalBranch', localBranch: 'master'],
                                        [$class: 'CleanCheckout'],
                                        [$class: 'MessageExclusion', excludedMessage: 'Preparing next snapshot'],
                                        [$class: 'MessageExclusion', excludedMessage: 'Release']
                                ],
                                submoduleCfg: [],
                                userRemoteConfigs: [[url: 'git@github.com:glipecki/watson.git']]]
                )
            }
        }
        stage('Build release version') {
            steps {
                sh './mvnw versions:set versions:commit -DremoveSnapshot'
                sh ' ./mvnw clean install'
                sh 'echo $(./mvnw help:evaluate -Dexpression=project.version 2>/dev/null | grep -v "\\[" | sed -n 2p) > version.txt'
                sh 'sed -i"" s/RELEASE_VERSION/$(cat version.txt)/g CHANGELOG.md'
                archiveArtifacts '**/watson-web*.jar'
                junit '**/target/surefire-reports/**/*.xml'
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
                sh 'git add .'
                sh 'echo $(./mvnw help:evaluate -Dexpression=project.version 2>/dev/null | grep -v "\\[" | sed -n 2p) > version.txt'
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
