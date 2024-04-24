## McSudo
这个模组在游戏中添加了一些常用了Linux命令，包括`sudo`,`echo`,`cat`,`export`。

同样也添加了一些Linux的机制，如`home`文件夹,`.bashrc`文件。

## 使用方法
### 参数
部分命令支持参数，你可以使用`_`加参数的形式来使用。
### 变量
部分命令也支持使用变量，你可以使用`${变量名}`的形式来使用变量，如何设置变量请见[export](#export)。

#### 动态变量
动态变量在每次执行时都会重新计算，以下是所有动态变量:
- `home`: 当前用户的home文件夹
- `uuid`: 当前用户的uuid
- `player`: 当前用户的用户名
- `exp`: 当前用户的经验值
- `level`: 当前用户的等级
- `health`: 当前用户的生命值
- `max_health`: 当前用户的最大生命值
- `world`: 当前世界名
- `location`: 当前位置（格式为x.y.z）
- `server`: 当前服务器名
- `motd`: 当前服务器的motd
- `time`: 当前时间（Unix时间戳）
### 命令
#### echo
用法:
```
/echo <内容>  # 输出内容
```
#### cat
用法:
```
/cat <文件路径>  # 输出文件内容
```
#### write
用法:
```
/write <文件路径> <内容>  # 写入文件内容，会覆盖原有内容
```
#### export
用法:
```
/export <变量名> <变量值>  # 设置变量
/export public_<变量名> <变量值> # 设置公共变量变量，需要sudo权限
```
#### apt
从Modrinth安装模组

用法:
```
/apt install <slug> <版本> # 安装模组
/apt search <slug> # 搜索模组
```
### 特殊文件
#### .bashrc
这个文件在玩家登录时执行里面的命令，命令以`;`分割且命令前不带`/`。

文件位于`home/<玩家名称>/.bashrc`，玩家不需要sudo权限就可以修改这个文件。
