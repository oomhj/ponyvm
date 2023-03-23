int main(){
unsigned int *tty = (unsigned int *)0x000000FF;
int i;
double pi = 0;

for(i = 0;i < 10000;i++){
    pi += (4.0 * (i % 2 ? -1 : 1)) / (2 * i + 1);
}


*tty = (int)(pi*1000000);

return (int)(pi*1000000);


}

//注：此程序使用莱布尼茨级数法求π，计算精度与迭代次数有关，本例中取1000000次迭代。