int main(void)
{
	int len = 100;
	int _sum = 0;
	for(int i = 0; i <= len; i++){
	    _sum += i;
	}
	//打印地址
//	unsigned int *tty = (unsigned int *)0x000000FF;
//	*tty = _sum;
	return _sum;
}