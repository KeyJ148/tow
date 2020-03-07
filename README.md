# Tanks: Orchestra of War
## Описание проекта
Это небольшая 2D-аркада с видом сверху, рассчитанная на игру по сети с другими людьми. Каждому игроку под управление даётся танк. Цель игры - уничтожить танки других игроков и остаться в живых. Для помощи игрокам на карте периодически появляются специальные ящики-бонусы: 3 вида ящиков для смены оборудования танка и 1 ящик ремонтного набора. 

Ящики для смены оборудования позволяют игроку получить новый корпус, орудие или тип снарядов. В результате получается достаточно большое количество комбинаций, каждая из которых может найти своё применение в зависимости от ситуации. 

Скриншоты:

![](https://github.com/KeyJ148/TOW/tree/ac9eef5e33faf87c33c08794a601b6785996fa3f/archive/screenshots/1.bmp)
![](https://github.com/KeyJ148/TOW/tree/ac9eef5e33faf87c33c08794a601b6785996fa3f/archive/screenshots/2.bmp)
![](https://github.com/KeyJ148/TOW/tree/ac9eef5e33faf87c33c08794a601b6785996fa3f/archive/screenshots/3.bmp)

## Управление 
WS - движение вперёд/назад  
AD - поворот танка по оси  
ЛКМ - выстрел танка в направлении курсора  
1/2/3/4 - блокировать подбор ящиков (для корпуса/орудия/типа снаряда/ремонтного набора)  
F3 - информация о танке  
### Только для синглплеера
N - умереть  
T - сменить корпус  
G - сменить орудие  
B - сменить тип снарядов  
H - отремонтировать себя (40%)  
F - отремонтировать себя (полностью)  
V - увеличивает эффект вампиризма до максимума  

## Техническая часть
Игра использует Java 11.  
Для отображения графики используется библиотека LWJGL3, позволяющая обращаться к функциям OpenGL.   
Для работы игры используется самописный движок. После версии игры [3.0.2](https://github.com/KeyJ148/TOW/releases/tag/v3.0.2) разработка движка ведется в том же репозитории, что и игры. До этого он использовался одновременно для нескольких проектов и находился в отдельном [репозитории](https://github.com/KeyJ148/Engine).  
Для создания карт используется [редактор карт](https://github.com/KeyJ148/MapEditor). Он позволяет при помощи графического интерфейса манипулировать объектами на карте. Результат экспортируется в текстовый файл с названиями и свойствами размещенных объектов. При запуске игрового сервера этот файл доступен для выбора в качестве карты.  
### Движок
###### Игровой цикл
Циклы обновления состояния игрового мира (update) и отрисовки изображения (render) выполняются строго друг за другом. Интерполяция позиции объектов для отрисовки без обновления состояния игрового мира не используется. При каждом обновление мира в объекты передаётся количество наносекунд прошедших с последнего обновления.  На основе этой величины рассчитываются перемещения и другие изменения состояния объектов игрового мира. Это делается, чтобы избежать рассинхронизации состояния игрового мира у клиентов с разной частотой кадров. Для ограничения частоты кадров используется вертикальная синхронизация. В настройках можно задать делитель вертикальной синхронизации: 0 - без ограничения, 1 - частота монитора, 2 - половина частоты монитора и т.д. Если вертикальная синхронизация не поддерживается драйверами видеокарты (например, в некоторых Unix системах), то в настройках можно задать ограничение частоты кадров, которое будет выполняться при помощи отправки в сон основного игрового потока. Реализация функции ограничения частоты кадров учитывает особенности остановки потока, из-за которых не гарантируется, что поток во время выйдет из сна, поэтому поток останавливается насколько раз на всё уменьшающиеся промежутки времени.  
###### Обработка игрового мира 
Игровой мир разделён на локации. В один момент времени может быть активна только одна локация. У объектов на активной локации вызываются функции update и render. С целью экономии ресурсов локация разбивается на чанки и содержит список видимых чанков, которые надо отрисовывать. Чанки за границей экрана обновляются только в случае, если на них есть активные объекты, для которых надо вызывать функцию update. Список чанков хранится в отсортированном виде, что позволяет при помощи бинарного поиска получать активные чанки достаточно быстро, и как следствие размер игровых локаций ограничен только размером переменной позиции объекта.
###### Ресурсы
Конфигурационные файлы разделены на 2 группы: внутренние и внешние. Внутренние конфигурационные файлы при сборке проекта сохраняются в jar-файле, к ним относятся пути к текстурам, звукам и т.д. Внешние конфигурационные файлы находятся в корне проекта, а при их удаление восстанавливаются стандартные значения из внутренних конфигурационных файлов. Во внешних конфигурационных файлах хранятся пользовательские настройки звука, графики и т.д. Конфигурационные файлы представлены в формате JSON. Для работы с ними разработан класс, который при помощи дженериков позволяет целиком загрузить или сохранить данные из внутренних/внешних конфигурационных файлов в соответствующий объект.
###### Игровые объекты
Все объекты расположенные в локации наследуются от общего класса игровых объектов. Игровые объекты имеют функции update и render, вызываемые движком в соответствующий момент игрового цикла. До версии игры [2.0.0](https://github.com/KeyJ148/TOW/releases/tag/v2.0.0) класс игрового объекта имел сложную цепочку наследования, из-за которой были трудности с расширением его функционала при реализации конкретного игрового элемента. На данный момент при работе с игровыми объектами используется компонентно-ориентированный подход. Все компоненты реализуют общий интерфейс с функциями update и render.  
Существует 5 базовых компонентов:
* Position - содержит координаты объекта.
* Movement - содержит вектор движения объекта.
* Collision - содержит маску объекта и использует паттерн listener для оповещения о пересечение с другими объектами, использующими компонент Collision. Существует расширенная версия CollisionDirect, которая позволяет просчитывать точку столкновения для прямолетящих объектов (например, снарядов), что экономит вычислительные ресурсы. При этом одновременно выполняется отслеживание статических и динамических объектов, что дает возможность использовать этот компонент для медленных объектов, которые могут пересечься с другим объектом раньше, чем достигнут просчитанной точки столкновения.
* Rendering - содержит цвет, масштаб и элемент для отрисовки. От этого компонента наследуются компоненты Sprite и Animation. Используется паттерн flyweight, чтобы не выделять место в памяти для текстур и прочих общих неизменяемых данных на каждый игровой объект.
* Particles - система частиц, связанная с игровым объектом. Используется для отрисовки эффектов с большим количеством элементов (например, взрывы). Делится на систему частиц с текстурами и простыми геометрическими фигурами.
###### Клиент-серверное взаимодействие
Для создания сервера в конструктор необходимо передать класс, который реализует функцию обработки клиентских сообщений. Сообщение содержит ID и данные свойственные для указанного ID. Для подключения к серверу необходимо создать клиент, в который также передаётся класс реализующий обработку полученных от сервера сообщений. При подключение клиента создаётся сразу два соединения: TCP и UDP. Несмотря на то, что TCP соединение настроено на наименьшие задержки (например, включена функция tcp_nodelay), в случае потери пакетов происходят достаточно долгие задержки. Поэтому для критических к скорости, но не критических к доставке пакетов (например, текущее положение игрока) используется протокол UDP. В дальнейшем планируется полностью отказаться от протокола TCP и использовать только Reliable UDP протокол.
###### Использование движка
Движок является фреймворком и при его использовании происходит инверсия управления. Для запуска необходимо реализовать несколько интерфейсов и передать их в класс Loader. Одним из этих интерфейсов является GameInterface, содержащий функции init, update и render. Эти функции вызываются перед стартом основного игрового цикла, перед обновлением игрового мира и отрисовкой игрового мира соответственно. Второй основной способ вмешаться в процесс игры - это создание игрового объекта и переопределение у него функций update и render, либо же переопределение этих функций у любого из компонентов игрового объекта.
### Игра
###### Снаряжение
В процессе игры игрок использует различное снаряжение: корпус, оружие, снаряды. Добавление в игру снаряжения и его настройка были максимально упрощены. Свойства снаряжения описываются в конфигурационном файле. Свойства включают в себя как общие свойства для всего снаряжения определенного типа (например, скорость корпуса, маневренность, текстура и т.д.), так и специфичные для определенной механики (например, кол-во осколков у разрывного снаряда). При этом сама механика также описывается в конфигурационном файле. Для каждого типа снаряжения (корпус, орудие, снаряд) создан родительский класс, от которого наследуются все классы различных механик. При подборе игроком ящика со снаряжением, выбирается случайный конфигурационный файл из находящихся в папке с конфигурационными файлами данного снаряжения. Из этого файла считывается механика снаряжения, после чего при помощи рефлексии находится одноименный класс, который уже запрашивает параметры специфичные для данной механики. Этот механизм позволил избавить от указания вручную большого количества связей между механиками, конфигурационными файлами, текстурами и т.д. Для добавления нового снаряжения в игру достаточно просто поместить один конфигурационный файл в соответствующую папку. Для добавления новой механики достаточно просто унаследовать новый класс от родительского класса и после этого эту механику можно указывать в любом конфигурационном файле.
###### Эффекты
В игре присутствует множество снаряжения, которое может одновременно влиять на одни и те же характеристики игрока. Причём снаряжение как добавляет/вычитает значение характеристики, так и влияет на неё в процентном соотношение. Чтобы упростить подсчёт влияния снаряжения на игрока была разработана система эффектов. Любое событие влияющие на игрока накладывает на него соответствующий эффект. Эффект содержит описание всех изменений характеристик. А для подсчёта значения характеристики вначале просчитываются все эффекты сложения, потом умножения данной характеристики. Таким образом, классу игрока нет необходимости знать какие именно классы и механики накладывают на него эффекты.