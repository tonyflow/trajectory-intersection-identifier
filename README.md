# Trajectory Intersection Identifier

This application is used to identify meeting points (intersections) by analysing the trajectories of people throughout the day and
can also be consudered as a starting point to provide insights regarding potential collaborations between different users.
The trajectories are defined by a list of coordinates provided in CSV format by the user. The form of the CSV is the following:
```csv
timestamp,x,y,floor,uid
2014-07-20T07:49:20.256Z,110.06576,88.51234456683532,2,661f7708
2014-07-20T08:21:14.700Z,110.06576,88.51234456683532,2,de0a13a8
2014-07-20T08:23:14.775Z,110.06576,88.51234456683532,2,de0a13a8
...
```
The definition of a ***meeting*** is the following
> Two trajectories t(1) and t(2) are considered as intersecting when the Euclidean distance 
of planar points pA of t(1) and pB of t(2) is smaller than `spatialEpsilon`, the interval between 
the timestamps of when these two points were recorded is smaller than `temporalEpsilon` and these
points were recorded on the ***same*** floor.

The term intersection is used loosely in the context of this application since there might not be an 
actual collision of the UIDs given. We are searching after a close proximity than an exact coordinate match.

## Epsilons
The nomenclature was derived from the DBSCAN clustering algorithm where epsilon is the maximum radius
of the neighborhood from a core point p. In the same fashion:

`spatialEpsilon`: Defines the maximum Euclidean distance between two planar points in 
order for them to be considered as potential collision candidates. For an actual meeting of people
to take place this *epsilon* could be anywhere between 0 - 4, assuming the coordinates refer to distances
in meters. For an eye contact meeting throughout a period of a day, this epsilon might be up
to 10.

`temporalEpsilon`: The maximum absolute difference between their timestamps, the coordinates must
satisfy in order to be intersection point candidates. My main assumption is that this variable
represents ***seconds*** to 1 minute maximum. There is no point in assuming that two people might have
ran into each other if they crossed the same point more than 1 minute apart.

## Algorithms
### Brute Force
The brute force algorithm approaches the identification of a potential collision by iteratively traversing
the entirety of the lists representing the coordinates corresponding to the two UIDs under examination. For each
coordinate of the first list, the Euclidean distance to every coordinate of the second list is
calculated and if this is smaller than the specified `spatialEpsilon` then their timestamps are
checked against the provided `temporalEpsilon` and their floors are checked for equality.

### Gabriel Graph / Delaunay Triangulation
This approach solves the minimum distance between two sets of planar points in O(nlogn). Computation is based on 
[Toussaint & Bhattacharya](http://www-cgrl.cs.mcgill.ca/~godfried/publications/mindist.pdf)'s paper. The paper describes 
an algorithm which:
- Calculates the Voronoi diagram of the union of the planar sets
- From the Voronoi diagram we deduce its dual graph: the Delaunay triangulation
- A subgraph of the Delaunay graph is the Gabriel graph which has the very interesting property where any 
points P and Q are adjacent if they are distinct and the line segment PQ is a circle's diameter containing no 
other elements of S, making P and Q the closest to each other. (where S is the union of the two planar sets 
 of points)
- After constructing the Gabriel graph we exclude all edges between points of the same UIDs
- The remaining points are the candidates for potential collisions. We check these against the 
`temporalEpsilon` and the floors they are situated to produce the final result.

##### Additional remarks on the algorithms
The two algorithms might return different results as to *where* and *when* the "meeting" occurred since there might be more than
two collision points that satisfy the provided criteria. The *Gabriel Graph / Delaunay Triangulation* seems to be more 
accurate as to the `spatialEpsilon` it produces.

## Running the application
The application is a maven project using Spring as its implementation framework (mainly 
in order to clarify the dependencies of services). Build the project (`mvn clean install`)
and then you might either
- `java -jar target/trajectory-intesection-identifier-1.0-SNAPSHOT.jar <fully qualified path to the location hosting the reduced.csv file>`. The
 application jar resides under the project's target folder. Or
- Import the project in IntelliJ (IDE of implementation) and run the main class `TrajectoryIntersectionIdentifierApplication`. Do not forget
 to specify the fully qualified path to the location containing the `reduced.csv` as a command line argument.
---
The interactive shell will guide you through the insertion of the configuration properties.

*Upon startup the application will read the data contained in the `reduced.csv` and load them into
 a map for quick access and processing. This loading might take some time. During tests this took
 10 - 15 seconds approximately*.

A sample run of the application could look like the following:
```
Algorithm: gabriel
Provide spatial epsilon (<10): 3.4
Provide temporal epsilon in seconds (max = 60 seconds): 30
Provide first UID: de0a13a8
Provide second UID: 2f442867
UID 2f442867 intersected with de0a13a8 on floor 2
2f442867 coordinates during meeting: [103.39497,70.69760759861116], at 2014-07-20T09:32:50.146Z
de0a13a8 coordinates during meeting: [104.24592,71.02910103404918], at 2014-07-20T01:34:14.904Z
Time of computation: 75355 millis
Do you wish to continue? (yes / no): yes
Algorithm: brute
Provide spatial epsilon (<10): 3.4
Provide temporal epsilon in seconds (max = 60 seconds): 30
Provide first UID: de0a13a8
Provide second UID: 2f442867
UID de0a13a8 intersected with 2f442867 on floor 2
de0a13a8 coordinates during meeting: [110.06576,88.51234456683532], at 2014-07-20T08:21:14.700Z
2f442867 coordinates during meeting: [107.34722,90.00259809545824], at 2014-07-20T08:20:43.917Z
Time of computation: 24 millis
Do you wish to continue? (yes / no): yes
Algorithm: gabriel
Provide spatial epsilon (<10): 3.4
Provide temporal epsilon in seconds (max = 60 seconds): 30
Provide first UID: de0a13a8
Provide second UID: 8f719572
No intersection found between the given points
Time of computation: 19980 millis

```

##### For future reference
- An actual conversation might be taking place between two people whose paths intersected if there
are adjacent timestamps indicating proximity between them for a prolonged period of time.


