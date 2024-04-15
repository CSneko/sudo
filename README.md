## Sudo
这个模组在游戏中添加了一些常用了Linux命令，包括`sudo`,`echo`,`cat`,`export`。

同样也添加了一些Linux的机制，如`home`文件夹,`.bashrc`文件。

## 使用方法
部分命令支持参数，你可以使用`_`加参数的形式来使用。例如: 
`sudo _i`中,`i`为参数。

### 以下是所有命令的使用方法
| 命令     | 参数  | 用法                 | 描述       | 
|:-------|:----|:-------------------|:---------| 
| sudo   | -i  | sudo -i <password> | 解锁sudo权限 | 
| sudo   | set | sudo set <content> | 设置环境变量   |
| echo   | 无   | echo <content>     | 输出内容     |
| cat    | 无   | cat <file>         | 输出文件内容   |
| export | 无   | export 变量名=变量值     | 设置环境变量   |