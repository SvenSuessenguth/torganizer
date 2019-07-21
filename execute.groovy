def call(instruction) {
  def os = System.properties['os.name'].toLowerCase()
  echo "OS: ${os}"
  if (os.contains("linux")) {
    sh instruction
  } else {
    bat instruction
  }    
}