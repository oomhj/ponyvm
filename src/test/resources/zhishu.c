int main()
{
unsigned int *tty = (unsigned int *)0x000000FF;
int i, j, flag;
// 从2枚举到1000，判断每个数是否为质数
for(i=2; i<=1000; i++)
{
    flag = 0; // 标记是否为质数
    // 在2到i-1之间找一个能整除i的数，如果找到了，就说明i不是质数
    for(j=2; j<i; j++)
    {
        if(i % j == 0)
        {
            flag = 1; // i不是质数
            break;
        }
    }
    if(flag == 0) // 判断i是否为质数
    {
         *tty = i; // 输出i，表示它是一个质数
    }
}
return 0;
}