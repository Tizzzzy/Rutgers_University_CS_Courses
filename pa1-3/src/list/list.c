#include<stdio.h>
#include<ctype.h>
#include<stdlib.h>
#include <string.h>

typedef struct nodes{
    /* data */
    int val;
    struct nodes* next;
}node;



int count(node* start){
    int c = 0;
    //node* ptr = head;
    start = start->next;
    while(start != NULL){
        c++;
        start = start -> next;
    }
    return c;
}

void print(node* start){
	int n=count(start);
    if (n == 0){
        //return;
        printf("0 :");
    }else{
        printf("%d :", n);
        start = start->next;
        while(start!=NULL){
            printf(" %d", start->val);
            start = start ->next;
        }
    }
    printf("\n");
}
void free_all(node* start){
    node* ptr = start, *temp = NULL;
    while(ptr != NULL){
        temp = ptr;
        ptr = ptr->next;
        free(temp);
    }
     //while(start != NULL){
       //  node* ptr = start;
         //start = start->next;
         //free(ptr);
     //}
}


int Num(char* command){
    //if(strlen(command) < 4){
      //  return EXIT_FAILURE;
    //}else{
        command += 2;
        int number = atoi(command);
        return number;
    //}
}

// void delete(node* head){
//     while(head != NULL){
//         node* ptr = head;
//         head = head->next;
//         free(ptr);
//     }
// }

int main(int argc, char** argv){

    node* start = malloc(sizeof(node));
    start->next = NULL;
    char com_line[100];
    while(fgets(com_line, 100, stdin) != NULL){
        if(com_line[0] == 'i'){
        if(strlen(com_line) < 3){
        free_all(start);
            return EXIT_FAILURE;
        }
            // *com_line += 2;
            // int number = atoi(*com_line);
            // return number;
            int number = Num(com_line);
            node* pre = start;
            node* ptr = start->next;
            
            while(ptr!=NULL){
                if(number == ptr->val){
                    break;
                }else if(number > ptr->val){
                    ptr = ptr->next;
                    pre = pre->next;
                }else if(number < ptr->val){
                    node* point = malloc(sizeof(node));
                    point->val = number;
                    point->next = ptr;
                    pre->next = point;
                    break;
                }
            }
            if(ptr == NULL){
                node* point = malloc(sizeof(node));
                point->val = number;
                pre->next = point;
                point->next = NULL;

            }
            
        }else if(com_line[0] == 'd'){
            if(strlen(com_line) < 3){
        free_all(start);
            return EXIT_FAILURE;
        }
            int number = Num(com_line);
            node* pre = start;
            node* ptr = start->next;
            //if(ptr == NULL){
              //  break;
            //}
            while(ptr != NULL){
                if(number == ptr->val){
                    pre->next = pre->next->next;
                    free(ptr);
                    break;
                }else if(number > ptr ->val){
                    pre = pre->next;
                    ptr = ptr->next;
                }else if(number < ptr -> val){
                    break;
                }
            }
            //if(ptr == NULL){
            	//printf("0 :\n");
            //}
        }else{
        	free_all(start);
        	return EXIT_FAILURE;
        }
        print(start);
    }
    free_all(start);
    return EXIT_SUCCESS;
}
