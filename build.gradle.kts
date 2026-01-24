import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
	java
	id("org.springframework.boot") version "3.5.6"
	id("io.spring.dependency-management") version "1.1.7"
    id("com.github.ben-manes.versions") version "0.53.0"
	id("com.github.bjornvester.wsdl2java") version "2.0.2"
}

group = "edu.pjatk"
version = "1.0.0-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
	maven(url = "https://build.shibboleth.net/maven/releases/")
}


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    testImplementation("org.mapstruct:mapstruct:1.6.3")
    testAnnotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    implementation("io.jsonwebtoken:jjwt-api:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    testAnnotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	implementation("com.bucket4j:bucket4j-core:8.10.1")
	implementation("com.github.ben-manes.caffeine:caffeine:3.2.3")

	implementation("org.apache.cxf:cxf-rt-frontend-jaxws:4.1.4")
	implementation("org.apache.cxf:cxf-rt-transports-http:4.1.4")
	implementation("org.apache.cxf:cxf-rt-ws-policy:4.1.4")
	implementation("org.apache.cxf:cxf-rt-ws-security:4.1.4")


}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<BootRun>().configureEach {
	systemProperty("javax.net.ssl.trustStore", "gus-truststore.jks")
	systemProperty("javax.net.ssl.trustStorePassword", "changeit")
}

wsdl2java {
	cxfVersion.set("4.1.4")
	packageName.set("edu.pjatk.gusregon.soap")
	wsdlDir.set(layout.projectDirectory.dir("src/main/resources/edu/pjatk/gusregon/soap"))
}