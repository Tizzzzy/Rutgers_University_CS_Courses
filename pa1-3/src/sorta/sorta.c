#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char** argv){
    char* temp;
    for(int i = 1; i < argc; i++){
        for(int j = i + 1; j < argc; j++){
            if(strcmp(argv[i], argv[j]) > 0){
                temp = argv[i];
                argv[i] = argv[j];
                argv[j] = temp;
            }
        }
    }
    for(int i = 1; i < argc; i++){
        printf("%s\n", argv[i]);
    }
    return EXIT_SUCCESS;
}
