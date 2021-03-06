import javaposse.jobdsl.dsl.DslFactory

DslFactory dsl = this

// Git credentials to use
String gitCredentials = binding.variables["GIT_CREDENTIAL_ID"] ?: "git"
String gitSshCredentials = binding.variables["GIT_SSH_CREDENTIAL_ID"] ?: "gitSsh"
Boolean gitUseSshKey= Boolean.parseBoolean(binding.variables["GIT_USE_SSH_KEY"] ?: "false")
// we're parsing the REPOS parameter to retrieve list of repos to build
String repos = binding.variables["REPOS"] ?: [
		"https://github.com/asokjp/claimant-service",
		"https://github.com/asokjp/config-server1",
		"https://github.com/asokjp/hello-world"
	].join(",")
List<String> parsedRepos = repos.split(",")
String jenkinsfileDir = binding.variables["JENKINSFILE_DIR"] ?: "${WORKSPACE}/jenkins/declarative-pipeline"

Map<String, Object> envs = [:]
envs['PIPELINE_VERSION_FORMAT'] = binding.variables["PIPELINE_VERSION_FORMAT"] ?: '''${BUILD_DATE_FORMATTED, \"yyMMdd_HHmmss\"}-VERSION'''
envs['PIPELINE_VERSION_PREFIX'] = binding.variables["PIPELINE_VERSION_PREFIX"] ?: '''1.0.0.M1'''
envs['PIPELINE_VERSION'] = binding.variables["PIPELINE_VERSION"] ?: ""
envs['GIT_CREDENTIAL_ID'] = gitCredentials
envs['GIT_SSH_CREDENTIAL_ID'] = gitSshCredentials
envs['GIT_USE_SSH_KEY'] = gitUseSshKey
envs['JDK_VERSION'] = binding.variables["JDK_VERSION"] ?: "jdk8"
envs['GIT_EMAIL'] = binding.variables["GIT_EMAIL"] ?: "pivo@tal.com"
envs['GIT_NAME'] = binding.variables["GIT_NAME"] ?: "Pivo Tal"
envs["PAAS_TYPE"] = binding.variables["PAAS_TYPE"] ?: "cf"
envs['TOOLS_REPOSITORY'] = binding.variables["TOOLS_REPOSITORY"] ?: 'https://github.com/asokjp/spring-cloud-pipelines1'
envs["TOOLS_BRANCH"] = binding.variables["TOOLS_BRANCH"] ?: "master"
envs["M2_SETTINGS_REPO_ID"] = binding.variables["M2_SETTINGS_REPO_ID"] ?: "artifactory-local"
envs["REPO_WITH_BINARIES"] = binding.variables["REPO_WITH_BINARIES"] ?: "http://artifactory:8081/artifactory/libs-release-local"
envs['REPO_WITH_BINARIES_CREDENTIAL_ID'] = binding.variables['REPO_WITH_BINARIES_CREDENTIAL_ID'] ?: ''
envs["AUTO_DEPLOY_TO_STAGE"] = binding.variables["AUTO_DEPLOY_TO_STAGE"] ?: false
envs["AUTO_DEPLOY_TO_PROD"] = binding.variables["AUTO_DEPLOY_TO_PROD"] ?: false
envs["API_COMPATIBILITY_STEP_REQUIRED"] = binding.variables["API_COMPATIBILITY_STEP_REQUIRED"] ?: true
envs["DB_ROLLBACK_STEP_REQUIRED"] = binding.variables["DB_ROLLBACK_STEP_REQUIRED"] ?: true
envs["DEPLOY_TO_STAGE_STEP_REQUIRED"] = binding.variables["DEPLOY_TO_STAGE_STEP_REQUIRED"] ?: true
// remove::start[CF]
envs['PAAS_TEST_CREDENTIAL_ID'] = binding.variables["PAAS_TEST_CREDENTIAL_ID"] ?: ""
envs['PAAS_STAGE_CREDENTIAL_ID'] = binding.variables["PAAS_STAGE_CREDENTIAL_ID"] ?: ""
envs['PAAS_PROD_CREDENTIAL_ID'] = binding.variables["PAAS_PROD_CREDENTIAL_ID"] ?: ""
envs["PAAS_TEST_API_URL"] = binding.variables["PAAS_TEST_API_URL"] ?: "api.local.pcfdev.io"
envs["PAAS_STAGE_API_URL"] = binding.variables["PAAS_STAGE_API_URL"] ?: "api.local.pcfdev.io"
envs["PAAS_PROD_API_URL"] = binding.variables["PAAS_PROD_API_URL"] ?: "api.local.pcfdev.io"
envs["PAAS_TEST_ORG"] = binding.variables["PAAS_TEST_ORG"] ?: "pcfdev-org"
envs["PAAS_TEST_SPACE"] = binding.variables["PAAS_TEST_SPACE"] ?: "pfcdev-test"
envs["PAAS_STAGE_ORG"] = binding.variables["PAAS_STAGE_ORG"] ?: "pcfdev-org"
envs["PAAS_STAGE_SPACE"] = binding.variables["PAAS_STAGE_SPACE"] ?: "pfcdev-stage"
envs["PAAS_PROD_ORG"] = binding.variables["PAAS_PROD_ORG"] ?: "pcfdev-org"
envs["PAAS_PROD_SPACE"] = binding.variables["PAAS_PROD_SPACE"] ?: "pfcdev-prod"
envs["PAAS_HOSTNAME_UUID"] = binding.variables["PAAS_HOSTNAME_UUID"] ?: ""
envs["APP_MEMORY_LIMIT"] = binding.variables["APP_MEMORY_LIMIT"] ?: ""
envs["JAVA_BUILDPACK_URL"] = binding.variables["JAVA_BUILDPACK_URL"] ?: "https://github.com/cloudfoundry/java-buildpack.git#v3.8.1"
// remove::end[CF]
// remove::start[K8S]
envs["PAAS_TEST_API_URL"] = binding.variables["PAAS_TEST_API_URL"] ?: "104.198.210.152:443"
envs["PAAS_STAGE_API_URL"] = binding.variables["PAAS_STAGE_API_URL"] ?: "104.198.210.152:443"
envs["PAAS_PROD_API_URL"] = binding.variables["PAAS_PROD_API_URL"] ?: "104.198.210.152:443"
envs["PAAS_TEST_CA"] = binding.variables["PAAS_TEST_CA"] ?: "/var/run/secrets/kubernetes.io/serviceaccount/ca.crt"
envs["PAAS_STAGE_CA"] = binding.variables["PAAS_STAGE_CA"] ?: "/var/run/secrets/kubernetes.io/serviceaccount/ca.crt"
envs["PAAS_PROD_CA"] = binding.variables["PAAS_PROD_CA"] ?: "/var/run/secrets/kubernetes.io/serviceaccount/ca.crt"
envs["PAAS_TEST_CLIENT_CERT"] = binding.variables["PAAS_TEST_CLIENT_CERT"] ?: ""
envs["PAAS_STAGE_CLIENT_CERT"] = binding.variables["PAAS_STAGE_CLIENT_CERT"] ?: ""
envs["PAAS_PROD_CLIENT_CERT"] = binding.variables["PAAS_PROD_CLIENT_CERT"] ?: ""
envs["PAAS_TEST_CLIENT_KEY"] = binding.variables["PAAS_TEST_CLIENT_KEY"] ?: ""
envs["PAAS_STAGE_CLIENT_KEY"] = binding.variables["PAAS_STAGE_CLIENT_KEY"] ?: ""
envs["PAAS_PROD_CLIENT_KEY"] = binding.variables["PAAS_PROD_CLIENT_KEY"] ?: ""
envs["PAAS_TEST_CLIENT_TOKEN_ID"] = binding.variables["PAAS_TEST_CLIENT_TOKEN_ID"] ?: ""
envs["PAAS_STAGE_CLIENT_TOKEN_ID"] = binding.variables["PAAS_STAGE_CLIENT_TOKEN_ID"] ?: ""
envs["PAAS_PROD_CLIENT_TOKEN_ID"] = binding.variables["PAAS_PROD_CLIENT_TOKEN_ID"] ?: ""
envs["PAAS_TEST_CLIENT_TOKEN_PATH"] = binding.variables["PAAS_TEST_CLIENT_TOKEN_PATH"] ?: "/var/run/secrets/kubernetes.io/serviceaccount/token"
envs["PAAS_STAGE_CLIENT_TOKEN_PATH"] = binding.variables["PAAS_STAGE_CLIENT_TOKEN_PATH"] ?: "/var/run/secrets/kubernetes.io/serviceaccount/token"
envs["PAAS_PROD_CLIENT_TOKEN_PATH"] = binding.variables["PAAS_PROD_CLIENT_TOKEN_PATH"] ?: "/var/run/secrets/kubernetes.io/serviceaccount/token"
envs["PAAS_TEST_CLUSTER_NAME"] = binding.variables["PAAS_TEST_CLUSTER_NAME"] ?: "gke_dulcet-hulling-185607_us-central1-c_test"
envs["PAAS_STAGE_CLUSTER_NAME"] = binding.variables["PAAS_STAGE_CLUSTER_NAME"] ?: "gke_dulcet-hulling-185607_us-central1-c_test"
envs["PAAS_PROD_CLUSTER_NAME"] = binding.variables["PAAS_PROD_CLUSTER_NAME"] ?: "gke_dulcet-hulling-185607_us-central1-c_test"
envs["PAAS_TEST_CLUSTER_USERNAME"] = binding.variables["PAAS_TEST_CLUSTER_USERNAME"] ?: "admin"
envs["PAAS_STAGE_CLUSTER_USERNAME"] = binding.variables["PAAS_STAGE_CLUSTER_USERNAME"] ?: "admin"
envs["PAAS_PROD_CLUSTER_USERNAME"] = binding.variables["PAAS_PROD_CLUSTER_USERNAME"] ?: "admin"
envs["PAAS_TEST_SYSTEM_NAME"] = binding.variables["PAAS_TEST_SYSTEM_NAME"] ?: "gke_dulcet-hulling-185607_us-central1-c_test"
envs["PAAS_STAGE_SYSTEM_NAME"] = binding.variables["PAAS_STAGE_SYSTEM_NAME"] ?: "gke_dulcet-hulling-185607_us-central1-c_test"
envs["PAAS_PROD_SYSTEM_NAME"] = binding.variables["PAAS_PROD_SYSTEM_NAME"] ?: "gke_dulcet-hulling-185607_us-central1-c_test"
envs["PAAS_TEST_NAMESPACE"] = binding.variables["PAAS_TEST_NAMESPACE"] ?: "sc-pipelines-test"
envs["PAAS_STAGE_NAMESPACE"] = binding.variables["PAAS_STAGE_NAMESPACE"] ?: "sc-pipelines-stage"
envs["PAAS_PROD_NAMESPACE"] = binding.variables["PAAS_PROD_NAMESPACE"] ?: "sc-pipelines-prod"
envs["KUBERNETES_MINIKUBE"] = binding.variables["KUBERNETES_MINIKUBE"] ?: "false"
envs["MYSQL_ROOT_CREDENTIAL_ID"] = binding.variables["MYSQL_ROOT_CREDENTIAL_ID"] ?: ""
envs["MYSQL_CREDENTIAL_ID"] = binding.variables["MYSQL_CREDENTIAL_ID"] ?: ""
envs["DOCKER_REGISTRY_ORGANIZATION"] = binding.variables["DOCKER_REGISTRY_ORGANIZATION"] ?: "asokjp"
envs["DOCKER_REGISTRY_CREDENTIAL_ID"] = binding.variables["DOCKER_REGISTRY_CREDENTIAL_ID"] ?: ""
envs["DOCKER_SERVER_ID"] = binding.variables["DOCKER_SERVER_ID"] ?: "docker-repo"
envs["DOCKER_EMAIL"] = binding.variables["DOCKER_EMAIL"] ?: "change@me.com"
envs["DOCKER_REGISTRY_URL"] = binding.variables["DOCKER_REGISTRY_URL"] ?: "https://index.docker.io/v1/"

parsedRepos.each {
	String gitRepoName = it.split('/').last() - '.git'
	String fullGitRepo = it
	String branchName = "master"
	int customNameIndex = it.indexOf('$')
	int customBranchIndex = it.indexOf('#')
	if (customNameIndex == -1 && customBranchIndex == -1) {
		// url
		fullGitRepo = it
		branchName = "master"
	} else if (customNameIndex > -1 && (customNameIndex < customBranchIndex || customBranchIndex == -1)) {
		fullGitRepo = it.substring(0, customNameIndex)
		if (customNameIndex < customBranchIndex) {
			// url$newName#someBranch
			gitRepoName = it.substring(customNameIndex + 1, customBranchIndex)
			branchName = it.substring(customBranchIndex + 1)
		} else if (customBranchIndex == -1) {
			// url$newName
			gitRepoName = it.substring(customNameIndex + 1)
		}
	} else if (customBranchIndex > -1) {
		fullGitRepo = it.substring(0, customBranchIndex)
		if (customBranchIndex < customNameIndex) {
			// url#someBranch$newName
			gitRepoName = it.substring(customNameIndex + 1)
			branchName = it.substring(customBranchIndex + 1, customNameIndex)
		} else if (customNameIndex == -1) {
			// url#someBranch
			gitRepoName = it.substring(it.lastIndexOf("/") + 1, customBranchIndex)
			branchName = it.substring(customBranchIndex + 1)
		}
	}
	String projectName = "${gitRepoName}-declarative-pipeline"
	
	envs['GIT_REPOSITORY'] = fullGitRepo
	envs['GIT_BRANCH_NAME'] = branchName

	dsl.pipelineJob(projectName) {
		wrappers {
			environmentVariables {
				environmentVariables(envs)
			}
		}
		definition {
			cps {
				script("""${dsl.readFileFromWorkspace(jenkinsfileDir + '/Jenkinsfile-sample')}""")
			}
		}
	}
}
