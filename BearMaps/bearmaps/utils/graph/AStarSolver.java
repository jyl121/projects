package bearmaps.utils.graph;

import bearmaps.utils.pq.DoubleMapPQ;
import bearmaps.utils.pq.MinHeapPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.*;
import java.util.List;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    private SolverOutcome outcome;
    private double solutionWeight;
    private LinkedList<Vertex> solution = new LinkedList<>();
    private double timeSpent;
    private int numStatesExplored;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();

        HashMap<Vertex, Double> disTo = new HashMap<>();
        HashMap<Vertex, Vertex> prev = new HashMap<>();
        DoubleMapPQ<Vertex> fringe = new DoubleMapPQ<>();

        disTo.put(start, 0.0);
        fringe.insert(start, input.estimatedDistanceToGoal(start, end));

        Vertex curr = start;
        while (fringe.size() != 0 && !fringe.peek().equals(end)) {
            curr = fringe.poll();
            numStatesExplored += 1;

            if (timeout <= sw.elapsedTime()) {
                outcome = SolverOutcome.TIMEOUT;
                solutionWeight = 0;
                timeSpent = sw.elapsedTime();
                return;
            }
            for (WeightedEdge<Vertex> v : input.neighbors(curr)) {
//                if (input.neighbors(v.to()).isEmpty()) {
//                    continue;
//                }
                double newPath = disTo.get(curr) + v.weight();
                if (newPath < disTo.getOrDefault(v.to(), Double.MAX_VALUE) ) {
                    disTo.put(v.to(), newPath);
                    prev.put(v.to(), curr);
                    if (fringe.contains(v.to())) {
                        fringe.changePriority(v.to(), newPath + input.estimatedDistanceToGoal(v.to(), end));
                    } else {
                        fringe.insert(v.to(), newPath + input.estimatedDistanceToGoal(v.to(), end));
                    }
                }
            }
        }

        if (fringe.size() == 0) {
            outcome = SolverOutcome.UNSOLVABLE;
            timeSpent = sw.elapsedTime();
            solutionWeight = 0;
            return;
        }
        solution.addFirst(end);
        while (!curr.equals(start)) {
            solution.addFirst(curr);
            curr = prev.get(curr);
        }
        solution.addFirst(start);
        timeSpent = sw.elapsedTime();
        outcome = SolverOutcome.SOLVED;
        solutionWeight = disTo.get(end);
    }

    public SolverOutcome outcome() {
        return outcome;
    }

    public List<Vertex> solution() {
        return solution;
    }

    public double solutionWeight() {
        return solutionWeight;
    }

    public int numStatesExplored() {
        return numStatesExplored;
    }

    public double explorationTime() {
        return timeSpent;
    }

    public static void main(String[] args) {
        return;
    }
}
