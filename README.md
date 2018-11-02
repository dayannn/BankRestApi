# BankRestApi

### Возможные действия:
1. POST /bankaccount/{id} - Завести новый счет. На вход команда принимает параметр номер счета - 5-ти значное число

* Успешное выполнение:
```
curl -X POST -i localhost:8080/bankaccount/16
```
```
HTTP/1.1 200
Content-Length: 0
Date: Fri, 02 Nov 2018 22:31:59 GMT
```
* Аккаунт с таким id уже существует
```
curl -X POST -i localhost:8080/bankaccount/16
```
```
HTTP/1.1 409
Content-Type: text/plain;charset=UTF-8
Content-Length: 58
Date: Fri, 02 Nov 2018 22:33:19 GMT

Couldn't create an account with id = 16. It already exists
```
* Значение id находится вне допустимых значений

```
curl -X POST -i localhost:8080/bankaccount/-5
```
```
HTTP/1.1 400
Content-Type: text/plain;charset=UTF-8
Content-Length: 60
Date: Fri, 02 Nov 2018 22:33:56 GMT
Connection: close

Id -5 is not in range of allowed ids. Enter id in [1; 99999]
```
---

2. PUT /bankaccount/{id}/deposit - Внести сумму на счёт. На вход команда принимает 2 параметра - номер счета и сумму к зачислению (передается в json)
* Успешное выполнение:
```
curl -X PUT -i --header "Content-Type: application/json" -d "{"""money""":10}" localhost:8080/bankaccount/15/deposit
```
```
HTTP/1.1 200
Content-Length: 0
Date: Fri, 02 Nov 2018 22:41:51 GMT
```

* Аккаунт с таким id не найден
```
curl -X PUT -i --header "Content-Type: application/json" -d "{"""money""":1}" localhost:8080/bankaccount/1555/deposit
```

```
HTTP/1.1 404
Content-Type: text/plain;charset=UTF-8
Content-Length: 29
Date: Fri, 02 Nov 2018 23:05:55 GMT

Could not find employee 1555
```

* Указана неверная (отрицательная) сумма
```
curl -X PUT -i --header "Content-Type: application/json" -d "{"""money""":-10}" localhost:8080/bankaccount/155/deposit
```
```
HTTP/1.1 406
Content-Type: text/plain;charset=UTF-8
Content-Length: 58
Date: Fri, 02 Nov 2018 22:46:15 GMT

-10 is an incorrect amount of money, it should be positive
```
---
3. PUT /bankaccount/{id}/withdraw - Снять сумму со счёта. На вход команда принимает 2 параметра - номер счета и сумму снятия (передается в json)

* Успешное выполнение
```
curl -X PUT -i --header "Content-Type: application/json" -d "{"""money""":10}" localhost:8080/bankaccount/155/withdraw
```
```
HTTP/1.1 200
Content-Length: 0
Date: Fri, 02 Nov 2018 22:47:53 GMT
```

* Аккаунт с таким id не найден
```
curl -X PUT -i --header "Content-Type: application/json" -d "{"""money""":10}" localhost:8080/bankaccount/1555/withdraw
```
```
HTTP/1.1 404
Content-Type: text/plain;charset=UTF-8
Content-Length: 29
Date: Fri, 02 Nov 2018 23:05:55 GMT

Could not find employee 1555
```

* Указана неверная (отрицательная сумма)
```
curl -X PUT -i --header "Content-Type: application/json" -d "{"""money""":-10}" localhost:8080/bankaccount/155/withdraw
```
```
HTTP/1.1 406
Content-Type: text/plain;charset=UTF-8
Content-Length: 58
Date: Fri, 02 Nov 2018 22:48:39 GMT

-10 is an incorrect amount of money, it should be positive
```

* На счету недостаточно средств
```
curl -X PUT -i --header "Content-Type: application/json" -d "{"""money""":1000}" localhost:8080/bankaccount/155/withdraw
```
```
HTTP/1.1 406
Content-Type: text/plain;charset=UTF-8
Content-Length: 42
Date: Fri, 02 Nov 2018 22:48:54 GMT

Bank account 155 doesn't have 1000$
```
---
4. GET /bankaccount/{id}/balance - Узнать баланс. На вход команда принимает параметр номер счета - 5-ти значное число

* Успешное выполнение
```
curl -X GET -i localhost:8080/bankaccount/155/balance
```
```
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Fri, 02 Nov 2018 22:51:34 GMT

{"money":0}
```

* Аккаунт с таким id не найден
```
curl -X GET -i localhost:8080/bankaccount/1555/balance
```
```
HTTP/1.1 404
Content-Type: text/plain;charset=UTF-8
Content-Length: 29
Date: Fri, 02 Nov 2018 23:05:55 GMT

Could not find employee 15455
```

## Сборка приложения
Сборка выполняется в jar файл со встроенным tomcat контейнером.
Для получения jar-файла приложения необходимо выполнить команду
```
mvn clean package
```

Для запуска тестов необходимо выполнить команду 
```
mvn clean package
```

## Настройка приложения

Для настройки приложения необходимо изменить файлы конфигурации application.properties и test.properties, предназначенные для настройки приложения и тестов соответственно

Настройки:
```
server.port={номер порта} 
spring.datasource.url=jdbc:postgresql://localhost:5432/{название базы данных} 
spring.datasource.username={логин}
spring.datasource.password={пароль}
```

#### Настройка БД
Для хранения данных используется PostgreSQL, настроить базу данных под заданные параметры можно следующим образом

```
$ psql -h localhost -U postgres
> CREATE database server;
> CREATE database test;
> CREATE role program WITH password 'test';
> GRANT ON PRIVILEGES ON database server TO program;
> GRANT ON PRIVILEGES ON database test TO program;
> ALTER role program WITH login;
```
