#include<stdio.h>
#include<ctype.h>
#include<stdlib.h>
#include <string.h>

typedef struct nodes{
    /* data */
    int val;
    struct nodes* left;
    struct nodes* right;
}node;


int search(node* root, int value){
    if(root == NULL){
        //printf("absent");
        return -1;
    }
    if(value == root->val){
        //printf("present");
        return 1;
    }else if(value > root->val){
        return search(root->right, value);
        //root = root->right;
    }else{
        return search(root->left, value);
        //root = root->left;
    }
}

node* insert(node* root, int value){
    if(root == NULL){
        root = (node*)malloc(sizeof(node));
        root->val = value;
        root->left = NULL;
        root->right = NULL;
        printf("inserted\n");
        return root;
    }
    if(root->val == value){
        printf("not inserted\n");
        return root;
    }else if(value > root->val){
        root->right = insert(root->right, value);
    }else{
        root->left = insert(root->left, value);
    }
    return root;
}

node* Big(node* root){
	while(root->right!=NULL){
		root = root->right;
	}
	return root;
}

node* delete(node* root, int value){
/*
	if(root == NULL){
		return root;
	}
	if(value < root->val){
		root->left = delete(root->left, value);
	}else if(value > root->val){
		root->right = delete(root->right, value);
	}else{
		if(root->left == NULL){
			node* temp = root->right;
			free(root);
			return temp;
		}else if(root->right == NULL){
			node* temp = root->left;
			return temp;
		}
		while(root && root->left != NULL){
			root = root->left;
		}
		node* temp = root;
		root->val = temp->val;
		root->right = delete(root->right, temp->val);
	}
	return root;
}

*/
    if(root == NULL){
        //printf("absent\n");
        return root;
    }
    if(value == root->val){
        if(root->left == NULL && root->right == NULL){
            free(root);
            return NULL;
            //printf("delete\n");
        }else if(root->left != NULL && root->right == NULL){
            node* ptr = root->left;
            free(root);
            return ptr;
            //printf("delete\n");
        }else if(root->left == NULL && root->right != NULL){
            node* ptr = root->right;
            free(root);
            return ptr;
            //printf("delete\n");
        }else{
            //while(root->right != NULL){
                //root = root->right;
            //}
            node* biggest = Big(root->left);
            //free(root);
            root->val = biggest->val;
            root->left = delete(root->left, root->val);
            //free(root);
            // node* max = Max(root->left);
            // root->val = max->val;
            // root->left = delete(root->left, root->val);
        }     
    }else if(value < root->val){
        root->left = delete(root->left, value);
    }else{
        root->right = delete(root->right, value);
    }
    return root;
}



void print(node* root){
	if(root==NULL){
		return;
	}
	printf("(");
	print(root->left);
	printf("%d",root->val);
	print(root->right);
	printf(")");
    // int count = 0;
    // while(root != NULL){
    //     count++;
    // }
    
    // printf("(%d)", root->val);
    // if(root->left != NULL){
    //     return print(root->left);
    // }else{
    //     return print(root->right);
    // }
}

void free_all(node* root){
    if(root == NULL){
        return;
    }
    //else{
        free_all(root->left);
        free_all(root->right);
        free(root);
    //}
}

int Num(char* command){
    // if(strlen(command) <= 3){
    //     return EXIT_FAILURE;
    // }else{
        command += 2;
        int number = atoi(command);
        return number;
    // }
}

int main(int argc, char** argv){
    char com_line[100];
    node* root = NULL;
    while(fgets(com_line, 100, stdin) != NULL){
        if(com_line[0] == 'i'){
            if(strlen(com_line) < 3){
                free_all(root);
                 return EXIT_FAILURE;
            }
            int number = Num(com_line);
            root = insert(root, number);
        }else if(com_line[0] == 's'){
        if(strlen(com_line) < 3){
            free_all(root);
            return EXIT_FAILURE;
        }
            int number = Num(com_line);
            if(search(root, number) == 1){
                printf("present\n");
            }else{
                printf("absent\n");
            }
        }else if(com_line[0] == 'd'){
        if(strlen(com_line) < 3){
            free_all(root);
            return EXIT_FAILURE;
        }
            int number = Num(com_line);
            if(search(root, number) == 1){
                root = delete(root, number);
                printf("deleted\n");
            }else{
                printf("absent\n");
            }
        }else if(com_line[0] == 'p'){
            print(root);
            printf("\n");
        }else{
            return EXIT_FAILURE;
        }
    }
    free_all(root);
    return EXIT_SUCCESS;
}
