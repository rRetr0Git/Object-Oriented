#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#define SIZE 10000
struct Poly {
	int coeff;
	int degree;
	struct Poly *next;
};
struct Poly * PolyAdd(struct Poly *p1, struct Poly *p2) {
	struct Poly *head = p1;
	struct Poly *temp;
	while (1) {
		while (1) {
			if (p2->degree == p1->degree) {
				temp = p2->next;
				p1->coeff +=p2->coeff;
				break;
			}
			else if (p2->degree < p1->degree) {
				temp =p2->next;
				p2->next = &(*p1);
				break;
			}
			else if (p1->next==NULL) {
				temp =p2->next;
				p1->next = p2;
				break;
			}
			else if (p2->degree > p1->degree &&p2->degree < (*p1->next).degree) {
				temp =p2->next;
				p2->next = p1->next;
				p1->next = p2;
				break;
			}
			p1 = p1->next;
		}
		if (temp == NULL) {
			break;
		}
		else {
			p2 = temp;
		}
	}
	p1 = head;
	return p1;
}
struct Poly * REV(struct Poly *p) {
	struct Poly *head = p;
	struct Poly *t;
	while (1) {
		p->coeff =-p->coeff;
		t = p->next;
		if (t == NULL) {
			break;
		}
		else {
			p = p->next;
		}
	}
	return head;
}
void print(struct Poly *p) {
	printf("{");
	while (1) {
		if (p->coeff != 0) {
			printf("(%d,%d)",p->coeff,p->degree);
		}
		if (p->next != NULL) {
			p = p->next;
			printf(",");
		}
		else {
			break;
		}
	}
	printf("}");
}
int main() {
	char str1[SIZE];char str2[SIZE];
	char op[20];
	gets(str1);
	int i = 0,j = 0,n = 0,headnum = 0;
	struct Poly *heads[20];
	struct Poly *p, *q;
	//delate space
	for (i = 0; str1[i]!='\0'; i++) {
		if (str1[i] != ' ') {
			str2[j] = str1[i];
			j++;
		}
	}
	str2[j] = '\0';
	for (i = 0; i < strlen(str2); i++) {
		//operation
		if (i == 0) {
			if (str2[0] == '-') {
				op[n++] = '-';
			}
			else {
				op[n++] = '+';
			}
		}
		if (str2[i] == '}') {
			if (str2[i + 1] == '\0') {
				op[n] = '\0';
			}
			else {
				op[n++] = str2[i + 1];
			}
		}
		//coeff and degree
		else if (str2[i] == '{') {
			p = (struct Poly*)malloc(sizeof(struct Poly));
			p->coeff = 0;
			p->degree = 0;
			p->next = NULL;
			heads[headnum++] = p;
			while (str2[i + 1] != '}') {
				int a = 0;
				int coeff=0;
				int degree = 0;
				if (str2[i] == '(') {
					i++;
					q= (struct Poly*)malloc(sizeof(struct Poly));
					int minus = 0;
					while (str2[i + a]!= ',') {
						if (str2[i + a] == '-') {
							minus = 1;
							a++;
						}
						else if (str2[i + a] >= '0'&&str2[i + a] <= '9') {
							coeff=coeff*10+(str2[i+a]-'0');
							a++;
						}
						else {
							a++;
						}
					}				
					if (minus) {
						q->coeff = -coeff;
					}
					else {
						q->coeff = coeff;
					}
					i = i + a +1;
					a = 0;
					while (str2[i + a] != ')') {
						if (str2[i + a] >= '0'&&str2[i + a] <= '9') {
							degree = degree * 10 + (str2[i + a] - '0');
							a++;
						}
						else {
							a++;
						}
					}
					q->degree = degree;
					i = i + a -1 ;
					q->next = NULL;
					p->next= q;
					p = q;
				}
				i++;
			}
		}
	}
	for (i = 0; i < n-1; i++) {
		if (i == 0) {
			if (op[0] == '+') {
				if (op[i+1] == '+') {
					heads[0]->next = PolyAdd(heads[0]->next, heads[1]->next);
				}
				else {
					heads[0]->next = PolyAdd(heads[0]->next, REV(heads[1]->next));
				}
			}
			else {
				if (op[i+1] == '+') {
					heads[0]->next = PolyAdd(REV(heads[0]->next), heads[1]->next);
				}
				else {
					heads[0]->next = PolyAdd(REV(heads[0]->next), REV(heads[1]->next));
				}
			}
		}
		else {
			if (op[i + 1] == '+') {
				heads[0]->next = PolyAdd(heads[0]->next, heads[i+1]->next);
			}
			else {
				heads[0]->next = PolyAdd(heads[0]->next, REV(heads[i + 1]->next));
			}
		}
	}
	print((*heads[0]).next);
	system("pause");
	return 0;
}