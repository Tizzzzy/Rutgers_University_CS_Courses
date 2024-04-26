#include <stdio.h>
#include <stdlib.h>

int max(int a, int b){
  if (a>b){ return a; }
  else{ return b; }
}

int main(int argc, char* argv[]){
  int limit = atoi(argv[1]);
  FILE *fp = NULL;
  fp = fopen(argv[2], "r");
  int nums;
  fscanf(fp, "%d", &nums);
  if (nums<0){
    fclose(fp);
    return 0;
  }
  char **names = (char**)malloc(sizeof(char*)*nums);
  for (int i=0;i<nums;i++){
    names[i] = (char*)malloc(sizeof(char)*32);
  }
  int *fees = (int*)malloc(sizeof(int)*nums);
  int *values = (int*)malloc(sizeof(int)*nums);
  for (int i=0;i<nums;i++){
    int n1, n2;
    fscanf(fp, "%31s %d %d", names[i], &n1, &n2);
    fees[i] = n1, values[i] = n2;
    //fscanf(fp, "%d", fees[i]);
    //fees[i] = atoi(buff);
    //fscanf(fp, "%d", values[i]);
    //values[i] = atoi(buff);
    if (values[i]<0 || fees[i]<0){
      fclose(fp);
      return 0;
    }
  }
  fclose(fp);
  int **path = (int**)malloc(sizeof(int*)*limit);
  for (int i=0;i<limit;i++){
    path[i] = (int*) malloc (sizeof(int)*nums);
  }
  
  if (limit<1){
    return 0;
  }
  for (int i=0;i<limit;i++){
    int l = i+1;
    for (int j=0;j<nums;j++){
      int tmp = 0, fee = fees[j], value = values[j];
      if (l<fee){
        if (j==0){ tmp = 0; }
        else{ tmp = path[i][j-1]; }
      }
      else{
        if (l==fee){
          if (j==0){ tmp = value; }
          else{ tmp = max(value, path[i][j-1]); }
        }
        else{
          if (j==0){ tmp = value; }
          else{ tmp = max(value+path[l-fee-1][j-1], path[i][j-1]);}
        }
      }
      path[i][j] = tmp;
    }
  }
/*
  for (int i=0;i<limit;i++){
    for (int j=0;j<nums;j++){
      printf("%d ", path[i][j]);
    }
    printf("\n");
  }
*/
  int ptn = limit-1;
  int *mark = (int*) malloc (sizeof(int)*nums);
  for (int j=nums-1;j>=0;j--){
    int flag = 0;
    if (j==0){
     	flag = 1;
    }
    else if (path[ptn][j] != path[ptn][j-1]){
	flag = 1;
    }
    if (flag == 1){
      int sum = 0;
      for (int k=0;k<nums;k++){
	if (mark[k]==1){
	  sum += values[k];
	}
      }
      if (sum < path[limit-1][nums-1]){
        mark[j] = 1; 
        if (ptn==0){
          break;
        }
        for (int i=0;i<ptn;i++){
	  if (j==0){
            break;
	  }
          if (path[i][j-1] == path[ptn][j]-values[j]){
	    ptn = i;
            break;
          }
        }
      }
    }
  }
  for (int i=0;i<nums;i++){
    if (mark[i]==1){
      printf("%s\n", names[i]);
    }
  }
  printf("%d / %d\n", path[limit-1][nums-1], limit);

  free(fees);
  free(values);
  free(mark);
  for(int i=0;i<nums;i++){
    free(names[i]);
  }
  free(names);
  for (int i=0;i<limit;i++){
    free(path[i]);
  }
  free (path);
  return 0;
}


