#!groovy
 
import jenkins.model.*
import hudson.security.*
import hudson.util.*;
import jenkins.install.*;
 
def instance = Jenkins.getInstance()
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
 
// ---------------------------------------------------------------------------------------------------------------------------------
// Create default user
// http://www.riptutorial.com/jenkins/example/24924/create-default-user
// ---------------------------------------------------------------------------------------------------------------------------------
hudsonRealm.createAccount("admin", "admin")
instance.setSecurityRealm(hudsonRealm) 
def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
instance.setAuthorizationStrategy(strategy)
instance.save()

// ---------------------------------------------------------------------------------------------------------------------------------
// Allow configuration of jenkins proxy host and proxy port
// https://github.com/geerlingguy/ansible-role-jenkins/issues/28
// ---------------------------------------------------------------------------------------------------------------------------------
//final String name = "proxy.system.local"
//final int port = 80
//final String userName = "techuser"
//final String password = "gF5!aL"
//final String noProxyHost = "*.system.local \n*.system-a.local \nlocalhost \n127.0.0.1"
// final def pc = new hudson.ProxyConfiguration(name, port, userName, password, noProxyHost)
// instance.proxy = pc
// instance.save()