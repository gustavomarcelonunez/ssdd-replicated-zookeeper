# Trabajo Final SSDD - Apache ZooKeeper

## Ejemplo básico de utilización de ZooKeeper para procesos distribuidos

**Introducción** 

El siguiente código de ejemplo es el trabajo final correspondiente a la materia Sistemas Distribuidos, materia perteneciente a la carrera Licenciatura en Informática que se dicta en la Universidad Nacional de la Patagonia San Juan Bosco, en su sede Puerto Madryn.

En este trabajo se realizará un ejemplo del proyecto de software Apache ZooKeeper, el cual implementa una cola productor-consumidor utilizando el servicio de ZooKeeper. 

El servicio se utiliza para proporcionar la coordinación necesaria entre el productor y el consumidor. El código tiene una clase principal SyncPrimitive y una clase interna Queue, que extiende SyncPrimitive.

La clase SyncPrimitive actúa como un bloqueo mutuo que sincroniza el acceso a las funciones compartidas por el productor y el consumidor. La clase Queue utiliza el bloqueo mutuo para sincronizar la adición y eliminación de elementos en la cola. La cola es una estructura FIFO.

La función principal del programa es queueTest, que se llama con los siguientes argumentos:

- args[0]: "qTest", lo que indica que se ejecutará la prueba de la cola.
- args[1]: la dirección IP y el número de puerto de ZooKeeper (para este ejemplo, localhost).
- args[2]: el número máximo de elementos que se agregarán o eliminarán de la cola.
- args[3]: "p" para el productor y "c" para el consumidor.

La función queueTest crea una instancia de la clase Queue y llama a la función *produce* o *consume* según el valor de args[3]. La función *produce* agrega N elementos a la cola, donde N es el valor especificado en args[2]. Por otro lado, la función *consume* elimina los N primeros elementos de la cola, donde N es el valor especificado en args[2].
 
En la siguiente sección de describe la preparación y puesta en marcha para correr el código de ejemplo.
<br><br>

**Prerrequisitos**

Antes de realizar la instalación, verificar la lista de requerimientos en el siguiente [enlace](https://zookeeper.apache.org/doc/r3.4.13/zookeeperAdmin.html#sc_systemReq).
<br>
Nota: Las librerías de ZooKeeper ya se encuentran dentro del proyecto. Si desea descargar la librería del sitio oficial diríjase al siguiente [enlace](https://zookeeper.apache.org/releases.html).

Desde aquí es posible configurar algunos parámetros desde un archivo de configuración ubicado en la carpeta *conf/zoo.cfg* (para más información sobre configuración ver el siguiente [tutorial](https://zookeeper.apache.org/doc/r3.4.13/zookeeperStarted.html#sc_InstallingSingleMode)).
<br><br>

## Compilación y ejecución

Para compilar el archivo ejecutable, desde la carpeta raiz ejecutar:

```
javac -cp ".:lib/*" src/SyncPrimitive.java
```
**Inciar el servidor local**

Para poder ejecutar el código de ejemplo, es necesario tener al menos una instancia de ZooKeeper corriendo. Para iniciar ZooKeeper escribir en la terminal:

```
bin/zkServer.sh start
```

Para poder acceder a nuestra instancia de ZooKeeper:
```
bin/zkCli.sh -server 127.0.0.1:2181
```

Para producir:
```
java -cp ".:lib/*" src/SyncPrimitive qTest localhost 100 p
```
Para consumir:
```
java -cp ".:lib/*" src/SyncPrimitive qTest localhost 100 c
```
El ejemplo consta de abrir dos terminales, en una ejecutar el consumidor (se queda esperando ya que no hay nada que consumir)
y en otra ejecutar el productor. 

Se puede utilizar una tercer terminal, e ingresando al servidor ZooKeeper, consultar los elementos que hay dentro del nodo creado.
<br><br>


# Tutorial para las instancias replicadas
Para levantar los contenedores Docker que harán la replicación del ejemplo, ubicarse en la raíz del proyect y ejecutar:
```
docker-compose up -d
```
Una vez finalizado, con el siguiente comando veremos los contenedores corriendo.
```
docker ps
```
Abrir tres terminales y en cada una acceder a los distintos contenedores:
```
docker exec -ti "nombre-del-contenedor" bash
```
Una vez dentro del contenedor acceder a su instancia de ZooKeeper, de la siguiente manera:

```
bin/zkCli.sh
```

Para saber qué servidor es el lider:

- Dentro del contenedor:
```
bin/zkServer.sh status
```
- Fuera del contenedor:
```
echo srvr | nc localhost "port" | grep Mode
```

A partir de aqui el procedimiento es igual a cuando se ejecuta el código de manera local, ya se pueden ejecutar productores y consumidores.

Los cambios realizados en cada instancia se replicarán en el resto.

# Extras
Tutorial del código: 
https://zookeeper.apache.org/doc/r3.7.1/zookeeperTutorial.html

Tutorial de configuración docker:
https://hub.docker.com/_/Zookeeper?tab=description

Información acerca de modo replicado:
https://zookeeper.apache.org/doc/current/zookeeperStarted.html#sc_RunningReplicatedZooKeeper

Comandos ZooKeeper:
https://zookeeper.apache.org/doc/r3.7.1/zookeeperStarted.html