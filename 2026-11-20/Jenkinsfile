pipeline {
    agent any

    tools {
        jdk 'jdk-25'
    }

    options {
        timestamps()
        timeout(time: 45, unit: 'MINUTES')
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '5'))
    }

    environment {
        GRADLE = 'gradlew.bat --console=plain --stacktrace'
    }

    stages {
        stage('Check tools') {
            steps {
                bat 'java -version'
                bat 'echo ANDROID_HOME=%ANDROID_HOME%'
                script {
                    if (!env.ANDROID_HOME) {
                        error 'ANDROID_HOME is not set.'
                    }
                }
                bat "${GRADLE} --version"
            }
        }

        stage('Verify') {
            steps {
                catchError(buildResult: 'UNSTABLE', stageResult: 'UNSTABLE') {
                    bat "${GRADLE} --continue lintDebug detekt ktlintCheck testDebugUnitTest createDebugUnitTestCoverageReport"
                }
            }
        }

        stage('Publish reports') {
            parallel {
                stage('Tests') {
                    steps {
                        junit testResults: 'app/build/test-results/testDebugUnitTest/*.xml',
                            allowEmptyResults: false
                    }
                }

                stage('Lint') {
                    steps {
                        recordIssues enabledForFailure: true,
                            tools: [androidLintParser(pattern: 'app/build/reports/lint-results-debug.xml')]
                    }
                }

                stage('detekt and ktlint') {
                    steps {
                        recordIssues enabledForFailure: true,
                            tool: checkStyle(id: 'detekt',
                                            name: 'detekt',
                                            pattern: 'app/build/reports/detekt/detekt.xml'
                                    ),
                            qualityGates: [[threshold: 1, type: 'NEW', unstable: true]]
                        recordIssues enabledForFailure: true,
                            tool: checkStyle(id: 'ktlint',
                                            name: 'ktlint',
                                            pattern: 'app/build/reports/ktlint/**/*.xml'
                                    ),
                            qualityGates: [[threshold: 1, type: 'NEW', unstable: true]]
                    }
                }

                stage('Coverage') {
                    steps {
                        bat 'powershell -NoProfile -ExecutionPolicy Bypass -File ci\\coverage-gate.ps1 -Report app/build/reports/coverage/test/debug/report.xml -Min 10'
                        recordCoverage(
                            tools: [[parser: 'JACOCO', pattern: 'app/build/reports/coverage/test/debug/report.xml']],
                            sourceCodeRetention: 'NEVER'
                        )
                    }
                }

            }
        }


        stage('Build debug APK') {
            steps {
                bat "${GRADLE} assembleDebug"
            }
            post {
                success {
                    archiveArtifacts artifacts: 'app/build/outputs/apk/debug/*.apk', fingerprint: true
                }
            }
        }

        stage('Sign release APK') {
            steps {
                withCredentials([
                    file(credentialsId: 'android-keystore', variable: 'KEYSTORE_FILE'),
                    string(credentialsId: 'android-store-password', variable: 'KEYSTORE_PASSWORD'),
                    usernamePassword(credentialsId: 'android-key', usernameVariable: 'KEY_ALIAS', passwordVariable: 'KEY_PASSWORD')
                ]) {
                    bat "${GRADLE} assembleRelease"
                }
                script {
                    env.APK_NAME = "jetpackdemos-${env.BUILD_NUMBER}-${(env.GIT_COMMIT ?: 'local').take(7)}.apk"
                }
                bat "copy /Y app\\build\\outputs\\apk\\release\\app-release.apk ${env.APK_NAME}"
                bat "powershell -NoProfile -ExecutionPolicy Bypass -File ci\\verify-apk.ps1 -Apk ${env.APK_NAME}"
            }
            post {
                success {
                    archiveArtifacts artifacts: 'jetpackdemos-*.apk', fingerprint: true
                }
            }
        }

    }

    post {
        always {
            echo "Finished with result: ${currentBuild.currentResult}"
        }
        failure {
            echo 'Build failed: open the first red stage and read the log from the bottom.'
        }
    }

}


