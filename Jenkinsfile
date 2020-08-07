node {
    
    try
    {

  def docker = tool name: 'dockerruntime', type: 'dockerTool'
  def docCMD = "${docker}/bin/docker"
  def mvnHome = tool name: 'Maven', type: 'maven'
  def mvnCMD = "${mvnHome}/bin/mvn"
  def imageName = "laxans16/springbootapp:1.0"

  stage('GIT Checkout') 
  {
        git credentialsId: 'GitHubPwd', url: 'https://github.com/lakshmanandevops/DevOpsSpringBootApp.git'
    
 }
 
    stage('Build with Sonar') {
        
        withSonarQubeEnv('SonarServ') 
          {
        sh "${mvnCMD} clean package sonar:sonar"
          }
         
    }

    stage('Docker Build') {
        
        sh "sudo ${docCMD} build -t ${imageName} ."
    }

    stage('Docker Push') {
        withCredentials([string(credentialsId: 'dockerPwd', variable: 'dockerHubPwd')]) {
            sh "sudo ${docCMD} login -u laxans16 -p ${dockerHubPwd}"
        }
        sh "sudo ${docCMD} push ${imageName}"
        sh "sudo ${docCMD} system prune -af"
    }

    

  stage('Launch EC2 via Ansible') {
    ansiblePlaybook becomeUser: 'ec2-user',
    credentialsId: 'aswcred',
    installation: 'Ansible',
    playbook: 'aws-ec2-launch.yml',
    sudoUser: 'ec2-user'
  }

  def ipAddress = "NULL"
  stage('Identify IP address') {
    def command = 'sudo aws ec2 describe-instances --query "Reservations[*].Instances[*].PublicIpAddress[]"'
    def output = sh script: "${command}",
    returnStdout: true
    def myIp = output.split('"')
    ipAddress = myIp[3]
    println ipAddress

  }

  stage('Install Docker on EC2') {
    def dockerCMD = 'sudo yum install docker -y'
    sshagent(['aws-server']) {
      sh "ssh -o StrictHostKeyChecking=no ec2-user@${ipAddress} ${dockerCMD}"
    }
  }

  stage('Run Docker Daemon') {
    def dockerCMD = 'sudo service docker start'
    sshagent(['aws-server']) {
       sh "ssh -o StrictHostKeyChecking=no ec2-user@${ipAddress} ${dockerCMD}"
    }
  }

  stage('Pull docker image') {
    def dockerCMD = "sudo docker pull ${imageName}"
    sshagent(['aws-server']) {
      sh "ssh -o StrictHostKeyChecking=no ec2-user@${ipAddress} ${dockerCMD}"

    }
  }

  stage('Run docker image') {
    def dockerCMD = "sudo docker run -p 8090:8090 -d ${imageName}"
    sshagent(['aws-server']) {
        sh "ssh -o StrictHostKeyChecking=no ec2-user@${ipAddress} ${dockerCMD}"

    }
  }
 } 
 catch (e) 
 {
     currentBuild.result = "FAILED"
    notifyFailed()
       
        throw e
 }
 
}


def notifyFailed() {
 
  emailext (
      to: 'lakshmanandevops@gmail.com', 
      subject: "Jenkins Job '${env.JOB_NAME}' FAILED ",
      body: """<p>There is a failure in the jenkins job :</p>
        <p>Please check the console output by clicking the link <a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a></p>""",
      recipientProviders: [[$class: 'DevelopersRecipientProvider']]
    )
    
}