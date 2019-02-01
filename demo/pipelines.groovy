folder("pipelines")

pipelines = ["puppet", "php"]

pipelines.each { pipeline ->

  folder("pipelines/${pipeline}")
  
  deliveryPipelineView("pipelines/${pipeline}/pipeline") {
    pipelineInstances(5)
    enableManualTriggers()
    showAvatars()
    showChangeLog()
    allowPipelineStart()
    pipelines {
        component("${pipeline} pipeline", "pipelines/${pipeline}/checkout")
    }
}

  job("pipelines/${pipeline}/checkout") {
    steps {
      shell('echo "dummy"')
      shell('echo "CHECKOUT_BUILD_NUMBER=$BUILD_NUMBER" > envs')
    }
    publishers {
      downstreamParameterized {
        trigger("pipelines/${pipeline}/validate") {
          condition("SUCCESS")
          parameters {
          propertiesFile("envs") 
          }
        }
      }
    }
  }

  job("pipelines/${pipeline}/validate") {
    parameters {
      stringParam("CHECKOUT_BUILD_NUMBER")
    }
    steps {
      shell('echo "dummy"')
      shell('echo "CHECKOUT_BUILD_NUMBER=$CHECKOUT_BUILD_NUMBER" > envs')
    }
    publishers {
      downstreamParameterized {
        trigger("pipelines/${pipeline}/build") {
       	  condition("SUCCESS")
          parameters {
            propertiesFile("envs") 
          }
        }
      }
    }
  }

  job("pipelines/${pipeline}/build") {
    parameters {
      stringParam("CHECKOUT_BUILD_NUMBER")
    }
    steps {
      shell('echo "dummy"')
      shell('echo "CHECKOUT_BUILD_NUMBER=$CHECKOUT_BUILD_NUMBER" > envs')
    }
    publishers {
      downstreamParameterized {
        trigger("pipelines/${pipeline}/deploy-dev") {
          condition("SUCCESS")
          parameters {
            propertiesFile("envs") 
          }
        }
      }
    }
  }

  job("pipelines/${pipeline}/deploy-dev") {
    parameters {
      stringParam("CHECKOUT_BUILD_NUMBER")
    }
    steps {
      shell('echo "dummy"')
      shell('echo "CHECKOUT_BUILD_NUMBER=$CHECKOUT_BUILD_NUMBER" > envs')
    }
    publishers {
      buildPipelineTrigger("pipelines/${pipeline}/deploy-uat") {
        parameters {
          propertiesFile("envs") 
        }
      }
    }
  }

  job("pipelines/${pipeline}/deploy-uat") {
    parameters {
      stringParam("CHECKOUT_BUILD_NUMBER")
    }
    steps {
      shell('echo "dummy"')
      shell('echo "CHECKOUT_BUILD_NUMBER=$CHECKOUT_BUILD_NUMBER" > envs')
    }
    publishers {
      buildPipelineTrigger("pipelines/${pipeline}/deploy-prod") {
        parameters {
          propertiesFile("envs") 
        }
      }
    }
  }

  job("pipelines/${pipeline}/deploy-prod") {
    parameters {
      stringParam("CHECKOUT_BUILD_NUMBER")
    }
    steps {
      shell('echo "dummy"')
    }
  }
}
