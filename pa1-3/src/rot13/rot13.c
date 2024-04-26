#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char** argv){
    //argv[1] - manipulate each charcter by 13
    //Ascii
    //fine end of string character \0

    //int pos = 0;
    //while (argv[1][pos] != '\0'){
    for(int pos = 0; pos < strlen(argv[1]); pos++){
        if((argv[1][pos] >= 'a' && argv[1][pos] < 'n') || (argv[1][pos] >= 'A' && argv[1][pos] < 'N')){
            argv[1][pos] += 13;
            printf("%c", argv[1][pos]);
        }else if((argv[1][pos] >= 'n' && argv[1][pos] <= 'z') || (argv[1][pos] >= 'N' && argv[1][pos] <= 'Z')){
            argv[1][pos] -= 13;
            printf("%c", argv[1][pos]);
        // }else if(argv[1][pos] == '!'){
        //     printf("!");
        }else{
            printf("%c", argv[1][pos]);
        }
        //pos++;
    }
    //printf("%s\n", argv[1]);
    printf("\n");
    return EXIT_SUCCESS;
}
