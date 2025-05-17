# Mini Custom Dependency Injection Framework (Java)

This project is a simplified version of Spring’s IoC (Inversion of Control) container built from scratch using Java. 

## Features Implemented

- Annotation-based dependency injection:
    - `@SimpleComponent` to register classes
    - `@SimpleAutoWired` for field, setter, or constructor injection
- Component scanning (via `@ComponentScan`)
- Dependency resolution and injection at runtime
- Support for:
    - Field injection
    - Setter injection
    - Constructor injection

##  How It Works

### Configuration Class

```java
@Configuration
@ComponentScan("com.chakir")
public class AppConfig {
}
```

```java
@SimpleComponent
public class Rep {
    Map<Integer, Test> testIdToTestMap = new HashMap<>();

    public Rep(){
        testIdToTestMap.put(1,new Test(1,"test1"));
        testIdToTestMap.put(2,new Test(2,"test2"));
        testIdToTestMap.put(3,new Test(3,"test3"));
        testIdToTestMap.put(4,new Test(4,"test4"));
    }

    public Test getById(Integer id) {
        return testIdToTestMap.get(id);
    }
}

```
```java
@SimpleComponent
public class Serv {
    @SimpleAutoWired
    private Rep repo;
    @SimpleAutoWired
    public Serv(Rep repo){
        this.repo = repo;
    }
    public Test test(Integer id){
        return repo.getById(id);
    }
}
```
```java
public class Main {
    public static void main(String[] args) throws Exception {
        AppContext appContext = new AppContext(AppConfig.class);
        Serv serv = appContext.getInstance(Serv.class);
        System.out.println(serv.test(2));
    }

}
```

## Dependency Injection Types Supported

* **Field Injection**
* **Setter Injection**
* **Constructor Injection** (automatically detects constructor and resolves dependencies)

## To Be Implemented

* XML-based configuration using JAXB
* Circular dependency handling


## Project Structure

```
src/
├── annotations/
│   ├── Configuration.java
│   ├── ComponentScan.java
│   ├── SimpleComponent.java
│   └── SimpleAutoWired.java
├── com.chakir/
│   ├── AppContext.java
│   ├── Main.java
│   └── AppConfig.java
├── com.chakir.repository/
│   └── Rep.java
├── com.chakir.service/
│   └── Serv.java
└── com.chakir.entity/
    └── Test.java
```
