#include <stdio.h>
#include <stdlib.h>

int isValid(int matrix[9][9]){

  int first[2] = {0, 0};
  
  for (int r = 0; r < 9; r++){
    int check[10]={0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    //int check[10];
    for (int c = 0; c < 9; c++){
      if (check[matrix[c][r]] != 0){
        return 0;
      }else {
        check[matrix[c][r]] = 1;
      }
    }
  }

  for (int r = 0; r < 9; r++){
    int check[10] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    //int check[10];
    for (int c = 0; c < 9; c++){
      if (check[matrix[r][c]] != 0){
        return 0;
      }else {
        check[matrix[r][c]] = 1;
      }
    }
  }
  
  for (int r = 0; r < 9; r+=3){
    first[1] = 0;
    for (int c = 0; c < 9; c+=3){
      int check[10]={0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
      //int check[10];
      for (int i = 0; i < 3; i++){
        for (int j = 0; j < 3; j++){
          if (check[matrix[first[0]+i][first[1]+j]] != 0){
            return 0;
          }else {
            check[matrix[first[0]+i][first[1]+j]] = 1;
          }
        }
      }
      first[1] += c;
    }
    first[0] += r;
  }
  return 1;
}

int complete(int matrix[9][9], int num){

 
  if (num == 2){
    int first[4] = {-1, -1, -1, -1};
    for (int r = 0; r < 9; r++){
      for (int c = 0; c < 9; c++){
        if (matrix[r][c] == 0){
          if (first[0] == -1){
            first[0] = r;
            first[1] = c;
          }
          else{
            first[2] = r;
            first[3] = c;
            break;
          }
        }
      }
    }
    for (int r = 1; r <= 9; r++){
      matrix[first[0]][first[1]] = r;
      for (int c = 1; c <= 9; c++){
        matrix[first[2]][first[3]] = c;
        if (isValid(matrix) != 0){
          return 1;
        }        
      }
    }
  }
   if (num == 1){
    int first[2] = {-1, -1};
    for (int r = 0; r < 9; r++){
      for (int c = 0; c < 9; c++){
        if (matrix[r][c] != 1){
          first[0] = r;
          first[1] = c;
          break;
        }
      }
    }
    for (int r = 1; r < 10; r++){
      matrix[first[0]][first[1]] = r;
      if (isValid(matrix) != 0){
        return 1;
      }
    }
  }
  return 0;
}

int main(int argc, char** argv){
  char c;
  int matrix[9][9];
  int pos = 0;
  //FILE *fp;
  FILE* fp = fopen(argv[1], "r");
    if (fp == NULL){
        return EXIT_FAILURE;
    }
  c = fgetc(fp);
  int x = 0;
  int y = 0;
  while(c != EOF){
    int num = c - '0';
    c = fgetc(fp);
    // if (num > 0){
    //   matrix[y][x] = num;
    // }else if  (num == -16){
    //   matrix[y][x] = 0;   
    //   pos ++;
    if (num == -16){
      matrix[y][x] = 0;   
      pos ++;
    }else if  (num > 0){
      matrix[y][x] = num;
    }else {
      continue;
    }
    x ++;
    if (x == 9){
        x = 0;
	      y ++;
    }
  }
  if (pos == 0){
    if (isValid(matrix) == 1){
      printf("correct\n");
      
    }
    else{
      printf("incorrect\n");
    }
  }
  else {
    if(complete(matrix, pos) == 1){
      printf("solvable\n");
      
    }
    else {
      printf("unsolvable\n");
    }
  }
  fclose(fp);
  return EXIT_SUCCESS;
}


