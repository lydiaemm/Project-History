/* COP 3502C Programming Assignment 3
This program is written by: Lydia Emmons */

/*Given your location, and the location of each monster, the list is sorted by distance from you from shortest to longest, breaking ties by x-coordinate (lower comes first), and then breaking those ties by y coordinate (lower comes first).
After sorting, it is determined whether there is a monster at several query points. If there is a monster, the monster's ranking is displayed.*/

#include <stdio.h>
#include <stdlib.h>
#include "leak_detector_c.h"

int MYx;
int MYy;

typedef struct coordinate
{
  int x;
  int y;
  int distance;
} coordinate;

int CalcDistance(int x, int y)
{
  //This function calculates the distance a coordinate point is from the user.
  
  int dist;
  dist = (x - MYx) * (x - MYx) + (y - MYy) * (y - MYy);
  return dist;
}

coordinate* ReadData(int x, int y)
{
  //This function reads in data and stores the monster's x-coordinate, y-coordinate, and distance from the user

  coordinate *new = malloc(sizeof(coordinate));
  new->x = x;
  new->y = y;
  new->distance = CalcDistance(x, y);
  return new;
}

int compareTo(coordinate *ptrPt1, coordinate *ptrPt2)
{
  //This function compares two coordinate struct pointers and returns a negative integer when the first is closer, a positive integer when the second is closer, and 0 when they are the same point
  
  int Pt1_x = ptrPt1->x;
  int Pt1_y = ptrPt1->y;
  int Pt2_x = ptrPt2->x;
  int Pt2_y = ptrPt2->y;
  
  int dist_Pt1 = ptrPt1->distance;
  int dist_Pt2 = ptrPt2->distance;
  
  if(dist_Pt1 < dist_Pt2)
    return -1;
  else if(dist_Pt1 > dist_Pt2)
    return 1;
  else if(Pt1_x == Pt2_x)
  {
    if(Pt1_y == Pt2_y)
      return 0;
    else if(Pt1_y < Pt2_y)
      return -1;
    else
      return 1;
  }
  else
  {
    if(Pt1_x < Pt2_x)
      return -1;
    else
      return 1;
  }
}

void insertSort(coordinate **arr, int l, int r)
{
  //This function inserts the monsters in order of their distance from the user.
  
  int i, j;
  for(i = l; i <= r; i++)
  {
    coordinate *curr;
    curr = arr[i];
    for(j = i-1; j >= l; j--)
    {
      if(compareTo(curr, arr[j]) <= 0)
      {
        arr[j+1] = arr[j];
        arr[j+1] = arr[j];
      }
      else
        break;
    }
    arr[j+1] = curr;
  }
}

void merge(coordinate **arr, int l, int m, int r)
{
  //This function merges arrays in the proper order that were separated in the mergeSort function.
  
  int i, j, k;
  int sizeL = m - l + 1;
  int sizeR = r - m;

  coordinate **L = malloc(sizeL * sizeof(coordinate*));
  coordinate **R = malloc(sizeR * sizeof(coordinate*));

  for(i = 0; i < sizeL; i++)
  {
    L[i] = arr[l + i];
  }
  for(j = 0; j < sizeR; j++)
  {
    R[j] = arr[m + 1 + j];
  }

  i = 0;
  j = 0;
  k = l;

  while(i < sizeL && j < sizeR)
  {
    if(compareTo(L[i], R[j]) <= 0)
    {
      arr[k] = L[i];
      i++;
    }
    else
    {
      arr[k] = R[j];
      j++;
    }
    k++;
  }
  while(i < sizeL)
  {
    arr[k] = L[i];
    i++;
    k++;
  }
  while(j < sizeR)
  {
    arr[k] = R[j];
    j++;
    k++;
  }
  free(L);
  free(R);
}

void mergeSort(coordinate **arr, int l, int r, int t)
{
  //This function sorts the monsters in order of their distance from the user.
  
  if (l < r)
  {
    int mid = (l + r)/2;
    if((r - l) <= t)
    {
      insertSort(arr, l, r);
    }
    else
    {
      mergeSort(arr, l, mid, t);
      mergeSort(arr, mid + 1, r, t);

      merge(arr, l, mid, r);
    }
    
  }
}

void mySort(coordinate **array, int length, int t)
{
  //This function calls the proper sorting function
  if(length <= t)
    insertSort(array, 0, length);
  else
    mergeSort(array, t, length, t);
}

int binarySearch(coordinate **list, int x, int y, int len)
{
  //This function searches the list of the monster's positions and returns their rank (based on distance from user).
  int left = 0, right = len-1;
  int mid;
  int dist = CalcDistance(x, y);

  while (left <= right)
  {
    mid = (left + right) / 2;
    
    if((list[mid]->x == x) && (list[mid]->y == y))
      return mid;
    else if(dist == list[mid]->distance)
    {
      if(list[mid]->x == x)
        if(list[mid]->y < y)
          left = mid + 1;
        else
          right = mid - 1;
      else
        if(list[mid]->x < x)
          left = mid + 1;
        else
          right = mid - 1;
    }
    else if(dist > list[mid]->distance)
    {
      left = mid + 1;
    }
    else
    {
      right = mid - 1;
    }
  }
  return -1;
}

void freememory(coordinate **arr, int length)
{
  //This function takes all the dynamically allocated structs and frees-up all the memory.
  
  for(int i = 0; i < length; i++)
    free(arr[i]);
  free(arr);
}

int main()
{
  //The main (driver) functions calls the UDFs in the proper order and calls them the proper number of times (based on the first line of input).
  
  atexit(report_mem_leak);
  int n, s, x, y, t, i;
  scanf("%d %d %d %d %d", &n, &s, &MYx, &MYy, &t);
  coordinate **arr = malloc(n * sizeof(coordinate*));
  for(i = 0; i < n; i++)
  {
    scanf("%d %d", &x, &y);
    arr[i] = ReadData(x, y);
  }
  
  
  mySort(arr, n-1, t);

  for(int j = 0; j < n; j++)
    printf("%d %d\n", arr[j]->x, arr[j]->y);
  
  
  for(int j = 0; j < s; j++)
  {
    scanf("%d %d", &x, &y); //read in queries
    int biSearch = binarySearch(arr, x, y, n);
    
    if(biSearch == -1)
    {
      printf("%d %d not found\n", x, y);
    }
    else
      printf("%d %d found at position %d\n", x, y, biSearch + 1);
  }

  freememory(arr, n);
  
  return 0;
}