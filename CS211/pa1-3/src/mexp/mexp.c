#include<stdio.h>
#include<ctype.h>
#include<stdlib.h>
#include <string.h>



void times(int* m1, int* m2, int n, int* m3){
    for(int r = 0; r < n; r++){
        for(int c = 0; c < n; c++){
            int answer = 0;
            for(int pos = 0; pos < n; pos++){
                answer += m1[r * n + pos] * m2[pos * n + c];
            }
            m3[r * n + c] = answer;
        }
    }
}

void print(int* m, int n){
    for(int r = 0; r < n; r++){
        for(int c = 0; c < n; c++){
            printf("%d", m[r * n + c]);
            if(c != n-1){
                printf(" ");
            }
        }
        printf("\n");
    }
}


void duplicate(int* m1, int* m2, int n){
    for(int r = 0; r < n; r++){
        for(int c = 0; c < n; c++){
            m1[r * n + c] = m2[r * n + c];
        }
    }
}

int main(int argc, char** argv){


    if(argc <= 1){
        return EXIT_FAILURE;
    }
    FILE* fp = fopen(argv[1], "r");
    if(fp == NULL){
        return EXIT_FAILURE;
    }
    int size, power;
    //char i[100];
    fscanf(fp, "%d\n", &size);
    int* m = (int*)malloc(sizeof(int) * size*size);
    for(int r = 0; r < size; r++){
        for(int c = 0; c < size; c++){
            fscanf(fp, "%d", m + r * size + c);
        }
    }
    int* m1 = (int*)malloc(sizeof(int) * size*size);
    duplicate(m1, m, size);
    int* m2 = (int*)malloc(sizeof(int) * size*size);
    duplicate(m1, m, size);
    //int power;
    
    fscanf(fp, "%d\n", &power);
    
    if(power == 0){
        for(int r = 0; r < size; r++){
            for(int c = 0; c < size; c++){
                if(r != c){
                    printf("0");
                    if(c != size-1){
                        printf(" ");
                    }
                }else{
                    printf("1");
                    if(c != size-1){
                        printf(" ");
                    }
                }
            }
            printf("\n");
        }


    }else if(power == 1){
        print(m, size);
    }else{
        for(int i = 0; i < power-1; i++){
            times(m1, m, size, m2);
            duplicate(m1, m2, size);
        }
        print(m2, size);
    }

    // power = power - 1;

    // for(int i = 0; i < power; i++){
    //     times(m1, m, size, m2);
    //     duplicate(m1, m2, size);
    // }
    // print(m2, size);
    fclose(fp);
    free(m);
    free(m1);
    free(m2);
    return EXIT_SUCCESS;
}
