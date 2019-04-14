plugins {
    java
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("javax:javaee-api:8.0.1-b5")
	implementation("org.eclipse.microprofile:microprofile:2.2")
	
    testRuntime("org.hibernate.validator:hibernate-validator:6.1.0.Alpha4")
	testRuntime("org.hibernate.validator:hibernate-validator-cdi:6.1.0.Alpha4")
	testRuntime("org.glassfish:javax.el:3.0.1-b11")
	
	testCompile("org.junit.jupiter:junit-jupiter-api:5.5.0-M1")
	testRuntime("org.junit.jupiter:junit-jupiter-engine:5.5.0-M1")
	testCompile("org.junit.jupiter:junit-jupiter-params:5.5.0-M1")
	testCompile("org.assertj:assertj-core:3.12.2")
	testCompile("org.mockito:mockito-core:2.26.0")
	testCompile("org.mockito:mockito-junit-jupiter:2.26.0")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}