# Modellierungstool

* [Heroku Application](http://graphical-modelchecker.herokuapp.com/)
* [Dokumentation](https://omueller54.github.io/Modeling)

## Examples

### Petrinet

* create network
    * http://graphical-modelchecker.herokuapp.com/petrinet/create/foo
    * http://graphical-modelchecker.herokuapp.com/petrinet/place/foo/bar/5
    * http://graphical-modelchecker.herokuapp.com/petrinet/transition/foo/xxx
    * http://graphical-modelchecker.herokuapp.com/petrinet/connect/foo/trans/bar/xxx/100
    * http://graphical-modelchecker.herokuapp.com/model/remove/foo
    
    ```json
    {
      "edges":[
        {
          "from":"bar",
          "to":"xxx",
          "capacity":100
        }
      ],
        
      "places":{
        "bar":{
          "id":"bar",
          "marks":[],
          "capacity":5
        }
      },

      "transitions":{
        "xxx": {
          "id":"xxx"
        }
      }
    }
    ```
    
### ERD (Entity Relationship Diagram)
* create network
    * http://graphical-modelchecker.herokuapp.com/erd/create/foo
    * http://graphical-modelchecker.herokuapp.com/erd/entity/foo/foo1/false
    * http://graphical-modelchecker.herokuapp.com/erd/connect/attribute/entity/foo/foo1/Vorname/false/false/false/false
    * http://graphical-modelchecker.herokuapp.com/erd/relationship/foo/rel01/besitzt/false/false
    * http://graphical-modelchecker.herokuapp.com/erd/connect/attribute/relationship/foo/rel01/Test/false/false/false/false
    * http://graphical-modelchecker.herokuapp.com/erd/connection/foo/con01/foo1/rel01/1:1
    
    ```json
    {
      "model" : "foo",
      "entities" : [
        {
          "id" : "foo1",
          "weakEntity" : false,
          "attributes" : [
            {
              "id" : "Vorname",
              "key" : false,
              "weakKey" : false,
              "multiVal" : false,
              "derivedVal" : false
            }
          ]
        }
      ],
      "relationships" : [
        {
          "id" : "rel01",
          "description" : "besitzt",
          "recursive" : false,
          "identifying" : false,
          "attributes" : []
        }
      ],
      "connections" : [
        {
          "id" : "con01",
          "from" : "foo1",
          "to" : "rel01",
          "cardinality" : "1:1"
        }
      ]
    }
    ```